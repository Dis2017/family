package top.gytf.family.server.security.access;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.exceptions.AccessDeniedException;
import top.gytf.family.server.security.access.provider.AccessProvider;
import top.gytf.family.server.utils.SecurityUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 访问决策拦截器<br>
 * CreateDate:  2021/12/12 21:48 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class AccessDecisionFilter extends OncePerRequestFilter {

    private final AccessProvider accessProvider;

    public AccessDecisionFilter(AccessProvider accessProvider) {
        this.accessProvider = accessProvider;
    }

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request request
     * @param response response
     * @param filterChain filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 取出uri+method
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 根据uri+method取出所需的权限
        Collection<? extends GrantedAuthority> needAuthorities = accessProvider.loadAuthorities(uri, method);

        // 取出用户具有的权限
        User user = SecurityUtil.current();
        Collection<? extends GrantedAuthority> authorities;
        if (user == null) {
            authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        } else {
            authorities = user.getAuthorities();
        }

        // 匹配权限
        for (GrantedAuthority needAuthority : needAuthorities) {
            if (authorities.contains(needAuthority)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 没匹配到
        throw new AccessDeniedException();
    }
}
