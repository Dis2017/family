package top.gytf.family.server.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.exceptions.IllegalArgumentException;
import top.gytf.family.server.exceptions.NotLoginException;
import top.gytf.family.server.security.login.email.EmailAuthenticationToken;
import top.gytf.family.server.security.login.password.PasswordToken;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 安全工具<br>
 * CreateDate:  2021/12/10 15:13 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class SecurityUtil {

    /**
     * 当前登录用户
     * @return 用户
     */
    public static User current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principalObj = null;
        if (authentication != null) {
            principalObj = authentication.getPrincipal();
        }
        if (!(principalObj instanceof User)) {
            throw new NotLoginException();
        }
        return (User) principalObj;
    }

    /**
     * 更新当前登录用户
     * @param user 用户
     */
    public static void update(User user) {
        if (!user.getId().equals(current().getId())) {
            throw new IllegalArgumentException("不能更新为其他用户");
        }

        // 清除密码
        user.setPassword(null);

        // 更新
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof EmailAuthenticationToken) {
            // 邮箱令牌
            authentication = new EmailAuthenticationToken(
                    user,
                    user.getAuthorities()
            );
        } else if (authentication instanceof PasswordToken) {
            // 账号密码令牌
            authentication = new PasswordToken(
                    user,
                    authentication.getCredentials(),
                    user.getAuthorities()
            );
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
