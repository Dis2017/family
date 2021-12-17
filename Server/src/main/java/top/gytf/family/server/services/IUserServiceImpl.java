package top.gytf.family.server.services;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gytf.family.server.constants.RoleEnum;
import top.gytf.family.server.entity.Role;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.entity.UserRole;
import top.gytf.family.server.exceptions.AvatarNotFoundException;
import top.gytf.family.server.exceptions.IllegalArgumentException;
import top.gytf.family.server.exceptions.curd.InsertException;
import top.gytf.family.server.exceptions.curd.UpdateException;
import top.gytf.family.server.file.FileManager;
import top.gytf.family.server.mapper.RolesMapper;
import top.gytf.family.server.mapper.UserMapper;
import top.gytf.family.server.mapper.UserRoleMapper;
import top.gytf.family.server.utils.UserUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: <br>
 * CreateDate:  2021/11/30 15:58 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Service
public class IUserServiceImpl implements IUserService {

    public static final RoleEnum DEFAULT_ROLE = RoleEnum.ROLE_USER;

    private final UserMapper userMapper;
    private final RolesMapper rolesMapper;
    private final UserRoleMapper userRoleMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 缺省的角色编号
     */
    private Long defaultRoleId;

    public IUserServiceImpl(UserMapper userMapper, RolesMapper rolesMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.rolesMapper = rolesMapper;
        this.userRoleMapper = userRoleMapper;
        resetDefaultRoleId();
    }

    /**
     * 用户是否存在<br>
     * 先查询是否存在id的用户，再查询手机号，最后查询邮箱<br>
     * 如果某项为null则跳过<br>
     *
     * @param id    用户的id
     * @param phone 用户的手机号
     * @param email 用户的邮箱
     * @return 是否存在
     */
    @Override
    public boolean exist(Long id, String phone, String email) {
        boolean exist = false;

        if (id != null) {
            exist = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getId, id)) == 1;
        }
        if (!exist && phone != null) {
            exist = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getId, id)) == 1;
        }
        if (!exist && email != null) {
            exist = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getId, id)) == 1;
        }

        return exist;
    }

    /**
     * 获取用户<br>
     * 先查询是否存在id的用户，再查询手机号，最后查询邮箱<br>
     * 如果某项为null则跳过<br>
     *
     * @param id    用户的id
     * @param phone 用户的手机号
     * @param email 用户的邮箱
     * @return 用户
     */
    @Override
    public User get(Long id, String phone, String email) {
        User user = null;

        if (id != null) {
            user = userMapper.selectById(id);
        }

        if (user == null && phone != null) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        }

        if (user == null && email != null) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        }

        return user;
    }

    /**
     * 更新用户
     *
     * @param id   更新的用户id
     * @param user 新的用户内容
     */
    @Override
    public void update(Long id, User user) {
        user.setId(id);
        UserUtil.clearProtectedMessage(user, true);
        if (userMapper.updateById(user) != 1) {
            throw new UpdateException();
        }
    }

    /**
     * 添加用户
     *
     * @param user 用户
     */
    @Override
    @Transactional(rollbackFor = InsertException.class)
    public void add(User user) {
        UserUtil.clearProtectedMessage(user, false);
        // 加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userMapper.insert(user) != 1 || userRoleMapper.insert(UserRole.builder()
                .userId(user.getId())
                .roleId(defaultRoleId)
                .build()) != 1) {
            throw new InsertException();
        }
    }

    /**
     * 绑定邮箱
     *
     * @param id    用户id
     * @param email 待绑邮箱地址
     */
    @Override
    public void bindEmail(Long id, String email) {
        if (!exist(id, null, null)) {
            throw new IllegalArgumentException("ID为" + id + "的用户不存在。");
        }
        if (exist(null, null, email)) {
            throw new IllegalArgumentException("邮箱" + email + "已被其他用户绑定");
        }
        String old = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id)).getEmail();
        if (old != null) {
            throw new IllegalArgumentException("ID为" + id + "的用户已存在一个绑定邮箱。");
        }

        if (userMapper.updateById(User.builder()
                        .id(id)
                        .email(email)
                .build()) != 1) {
            throw new UpdateException();
        }
    }

    /**
     * 解除邮箱绑定
     *
     * @param id 用户id
     */
    @Override
    public void unbindEmail(Long id) {
        if (userMapper.update(User.builder().id(id).build(),
                new LambdaUpdateWrapper<User>().set(User::getEmail, null)) != 1) {
            throw new UpdateException();
        }
    }

    /**
     * 绑定手机
     *
     * @param id    用户id
     * @param phone 待绑手机号
     */
    @Override
    public void bindPhone(Long id, String phone) {

    }

    /**
     * 解除手机绑定
     *
     * @param id 用户id
     */
    @Override
    public void unbindPhone(Long id) {

    }

    /**
     * 修改密码
     *
     * @param id       用户id
     * @param password 新密码
     */
    @Override
    public void modifyPassword(Long id, String password) {
        // 加密
        password = passwordEncoder.encode(password);
        if (userMapper.updateById(User.builder()
                .id(id)
                .password(password)
                .build()) != 1) {
            throw new UpdateException();
        }
    }

    /**
     * 获取密码
     *
     * @param id 用户编号
     * @return 密码
     */
    @Override
    public String getPassword(Long id) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .select(User::getPassword)
                        .eq(User::getId, id)
        ).getPassword();
    }

    /**
     * 获取头像
     *
     * @param id 用户编号
     * @return 头像
     */
    @Override
    public BufferedImage getAvatar(Long id) throws IOException {
        File avatar = FileManager.current().getUserFileSpace().getAvatar();
        if (!avatar.exists()) {
            throw new AvatarNotFoundException("头像未找到");
        }
        return ImageIO.read(avatar);
    }

    /**
     * 设置头像
     *
     * @param id     用户编号
     * @param avatar 头像
     * @throws IOException 写入错误
     */
    @Override
    public void setAvatar(Long id, BufferedImage avatar) throws IOException {
        ImageIO.write(avatar, "jpg", FileManager.current().getUserFileSpace().getAvatar());
    }

    /**
     * 重新设置缺省角色编号
     */
    private synchronized void resetDefaultRoleId() {
        Role role = rolesMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRole, DEFAULT_ROLE.name()));

        if (role == null) {
            throw new IllegalArgumentException("默认角色" + DEFAULT_ROLE + "不存在");
        }

        this.defaultRoleId = role.getId();
    }
}
