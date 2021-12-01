package top.gytf.family.server.security.id;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.services.IUserService;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户编号密码用户信息获取服务<br>
 * CreateDate:  2021/12/1 19:31 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class IdPasswordUserDetailsService implements UserDetailsService {
    private final static String TAG = IdPasswordUserDetailsService.class.getName();

    private final IUserService userService;

    public IdPasswordUserDetailsService(IUserService userService) {
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
        Long id = Long.parseLong(username);
        User user = userService.get(id, null, null);
        if (user == null) throw new UsernameNotFoundException("ID为" + id + "的用户不存在。");
        return user;
    }
}
