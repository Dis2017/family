package top.gytf.family.server.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.gytf.family.server.security.email.EmailAuthenticationToken;
import top.gytf.family.server.security.id.IdPasswordToken;

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
    private final static String TAG = SecurityUtil.class.getName();
    /**
     * 当前登录用户
     * @return 用户
     */
    public static top.gytf.family.server.entity.User current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principalObj = null;
        if (authentication != null) {
            principalObj = authentication.getPrincipal();
        }
        if (!(principalObj instanceof top.gytf.family.server.entity.User)) {
            return null;
        }
        return (top.gytf.family.server.entity.User) principalObj;
    }

    /**
     * 更新当前登录用户
     * @param user 用户
     */
    public static void update(top.gytf.family.server.entity.User user) {
        user.setPassword(null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof EmailAuthenticationToken) {
            EmailAuthenticationToken newToken = new EmailAuthenticationToken(
                    user,
                    authentication.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newToken);
        } else if (authentication instanceof IdPasswordToken) {
            IdPasswordToken newToken = new IdPasswordToken(
                    user,
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newToken);
        }
    }
}
