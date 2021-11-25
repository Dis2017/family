package top.gytf.family.server.security.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import top.gytf.family.server.constants.SessionConstant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   EmailAuthenticationFilter
 * Description: 拦截请求装配为EmailToken
 * CreateDate:  2021/11/25 16:09
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class EmailAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static String TAG = EmailAuthenticationFilter.class.getName();

    /**
     * 是否只接受Post请求
     */
    @Setter
    @Getter
    private boolean postOnly = true;

    /**
     * @param defaultFilterProcessesUrl the default value for <tt>filterProcessesUrl</tt>.
     */
    public EmailAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    /**
     * Creates a new instance
     *
     * @param requiresAuthenticationRequestMatcher the {@link RequestMatcher} used to
     *                                             determine if authentication is required. Cannot be null.
     */
    public EmailAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(new AntPathRequestMatcher("/auth/login/email", "POST"));
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
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        if (isPostOnly() && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("不接受非POST请求");
        }

        String email = getEmail(request).trim();
        EmailAuthenticationToken token = new EmailAuthenticationToken(email);
        copyDetails(token, request);

        return getAuthenticationManager().authenticate(token);
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

    /**
     * 取出request中的电子邮箱地址
     * @param request 请求
     * @return 电子邮箱地址
     */
    private String getEmail(HttpServletRequest request) {
        Object email = request.getSession().getAttribute(SessionConstant.KEY_AUTH_EMAIL);
        return !(email instanceof String) ? "" : (String) email;
    }
}