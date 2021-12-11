package top.gytf.family.server.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.gytf.family.server.entity.Role;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.entity.UserRole;
import top.gytf.family.server.mapper.RolesMapper;
import top.gytf.family.server.mapper.UserRoleMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户工具<br>
 * CreateDate:  2021/12/10 15:11 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class UserUtil {
    private final static String TAG = UserUtil.class.getName();

    /**
     * 清除保护数据<br>
     * <li>密码</li>
     * <li>邮箱</li>
     * <li>手机号</li>
     * <li>家庭id</li>
     * @param user 用户
     * @param clearPsd 是否清除密码
     */
    public static void clearProtectedMessage(top.gytf.family.server.entity.User user, boolean clearPsd) {
        if (clearPsd) {
            user.setPassword(null);
        }
        user.setEmail(null);
        user.setPhone(null);
        user.setFamilyId(null);
    }

    /**
     * 加载用户权限
     * @param user 用户实体
     * @param userRoleMapper userRoleMapper
     * @param rolesMapper rolesMapper
     * @return 添加完权限后的用户
     */
    public static User loadAuthorities(User user, UserRoleMapper userRoleMapper, RolesMapper rolesMapper) {
        List<Long> roleIdList = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                        .select(UserRole::getRoleId)
                        .eq(UserRole::getUserId, user.getId()))
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        user.setAuthorities(rolesMapper.selectBatchIds(roleIdList)
                .stream()
                .map(Role::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));

        return user;
    }
}
