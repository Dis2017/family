package top.gytf.family.server.security.email;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.services.IUserService;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   EmailUserDetailsService
 * Description: 邮箱用户信息服务
 * CreateDate:  2021/11/25 22:29
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class EmailUserDetailsService implements UserDetailsService {
    private final static String TAG = EmailUserDetailsService.class.getName();

    private final IUserService userService;

    public EmailUserDetailsService(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.get(null, null, username);
        if (user == null) throw new UsernameNotFoundException("邮箱" + username + "不存在。");
        return user;
    }
}
