package top.gytf.family.server.aop.user;

import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.exceptions.NotLoginException;
import top.gytf.family.server.mapper.RolesMapper;
import top.gytf.family.server.mapper.UserRoleMapper;
import top.gytf.family.server.services.IUserService;
import top.gytf.family.server.utils.SecurityUtil;
import top.gytf.family.server.utils.UserUtil;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户处理器<br>
 * CreateDate:  2021/12/17 21:12 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
@Aspect
@AllArgsConstructor
public class UserHandler {
    private final static String TAG = UserHandler.class.getName();

    private final IUserService userService;
    private final UserRoleMapper userRoleMapper;
    private final RolesMapper rolesMapper;

    /**
     * 所有更新当前认证信息注解的接口
     */
    @Pointcut("@annotation(UpdateCurrentAuthentication)")
    public void allUpdateCurrentAuthentication() {}

    /**
     * 更新当前用户信息
     */
    @AfterReturning("allUpdateCurrentAuthentication()")
    public void updateCurrentAuthentication() {
        try {
            User user = SecurityUtil.current();
            user = userService.get(user.getId(), null, null);
            UserUtil.loadAuthorities(user, userRoleMapper, rolesMapper);
            SecurityUtil.update(user);
        } catch (NotLoginException ignored) {
        }
    }
}
