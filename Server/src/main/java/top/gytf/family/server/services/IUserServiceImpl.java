package top.gytf.family.server.services;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import top.gytf.family.server.Utils;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.mapper.UserMapper;

import java.util.Objects;

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
    private final static String TAG = IUserServiceImpl.class.getName();

    private final UserMapper userMapper;

    public IUserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
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

        if (id != null)  exist = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getId, id)) == 1;
        if (!exist && phone != null)  exist = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getId, id)) == 1;
        if (!exist && email != null)  exist = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getId, id)) == 1;

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
     * @param user 新的用户内容
     */
    @Override
    public void update(User user) {
        Utils.User.clearProtectedMessage(user, true);
        userMapper.updateById(user);
    }

    /**
     * 添加用户
     *
     * @param user 用户
     */
    @Override
    public void add(User user) {
        Utils.User.clearProtectedMessage(user, false);
        userMapper.insert(user);
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

        userMapper.updateById(User.builder()
                        .id(id)
                        .email(email)
                .build());
    }

    /**
     * 解除邮箱绑定
     *
     * @param id 用户id
     */
    @Override
    public void unbindEmail(Long id) {
        userMapper.update(User.builder().id(id).build(),
                new LambdaUpdateWrapper<User>().set(User::getEmail, null));
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
        userMapper.updateById(User.builder()
                        .id(id)
                        .password(password)
                .build());
    }

    /**
     * 获取密码
     *
     * @param id 用户编号
     * @return 密码
     */
    @Override
    public String getPassword(Long id) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().select(User::getPassword).eq(User::getId, id)).getPassword();
    }
}
