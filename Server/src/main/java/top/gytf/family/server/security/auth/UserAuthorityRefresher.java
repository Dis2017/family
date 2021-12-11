package top.gytf.family.server.security.auth;

import org.springframework.context.event.EventListener;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.events.RefreshAuthorityCacheEvent;
import top.gytf.family.server.mapper.RolesMapper;
import top.gytf.family.server.mapper.UserRoleMapper;
import top.gytf.family.server.utils.UserUtil;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户权限刷新器<br>
 * CreateDate:  2021/12/12 1:36 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class UserAuthorityRefresher {
    private final static String TAG = UserAuthorityRefresher.class.getName();

    private final SessionRegistry sessionRegistry;
    private final UserRoleMapper userRoleMapper;
    private final RolesMapper rolesMapper;

    public UserAuthorityRefresher(SessionRegistry sessionRegistry, UserRoleMapper userRoleMapper, RolesMapper rolesMapper) {
        this.sessionRegistry = sessionRegistry;
        this.userRoleMapper = userRoleMapper;
        this.rolesMapper = rolesMapper;
    }

    @EventListener(RefreshAuthorityCacheEvent.class)
    public void refresh() {
        sessionRegistry.getAllPrincipals().stream()
                .filter((obj) -> obj instanceof User)
                .map((obj) -> (User) obj)
                .forEach((user) -> UserUtil.loadAuthorities(user, userRoleMapper, rolesMapper));
    }
}
