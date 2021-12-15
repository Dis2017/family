package top.gytf.family.server.security.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import top.gytf.family.server.security.login.email.EmailAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 认证过滤器<br>
 * CreateDate:  2021/12/15 23:34 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
//@Component
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static String TAG = AuthenticationFilter.class.getName();

    private final Collection<Authenticator> authenticators;

    public AuthenticationFilter(Collection<Authenticator> authenticators) {
        super(new AnyRequestMatcher(authenticators.stream()
                .map((authenticator) -> new AntPathRequestMatcher(authenticator.pattern(), authenticator.method().name()))
                .collect(Collectors.toSet())));
        this.authenticators = authenticators;
    }


    /**
     * Performs actual authentication.
     * <p>
     * The implementation should do one of the following:
     * <ol>
     * <li>Return a populated authentication token for the authenticated user, indicating
     * successful authentication</li>
     * <li>Return null, indicating that the authentication process is still in progress.
     * Before returning, the implementation should perform any additional work required to
     * complete the process.</li>
     * <li>Throw an <tt>AuthenticationException</tt> if the authentication process fails</li>
     * </ol>
     *
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     *                 redirect as part of a multi-stage authentication process (such as OpenID).
     * @return the authenticated user token, or null if authentication is incomplete.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        return null;
    }


    /**
     * 复制请求信息到token<br>
     * 包括IP、session等
     * @param token 令牌
     * @param request 请求体
     */
    private void copyDetails(EmailAuthenticationToken token, HttpServletRequest request) {
        token.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
