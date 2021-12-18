package top.gytf.family.server.security.login.password;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.security.login.email.EmailAuthenticationFilter;
import top.gytf.family.server.utils.RsaUtil;

import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 编号密码认证过滤器<br>
 * CreateDate:  2021/12/1 19:14 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class PasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * 是否只接受Post请求
     */
    @Setter
    @Getter
    private boolean postOnly = true;
    private final RsaUtil rsaUtil;
    private final PasswordAuthenticationParamGetter getter;

    public PasswordAuthenticationFilter(PasswordAuthenticationParamGetter getter) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        super(new AntPathRequestMatcher(PathConstant.Auth.AUTH_PREFIX + PathConstant.Auth.PATH_PASSWORD_LOGIN, "POST"));
        this.getter = getter;
        rsaUtil = RsaUtil.getInstance();
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
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (isPostOnly() && !EmailAuthenticationFilter.POST_METHOD_NAME.equals(request.getMethod())) {
            throw new AuthenticationServiceException("不接受非POST请求");
        }

        String credential = getter.getCredential(request);
        String password = getter.getPassword(request);
        PasswordToken token = new PasswordToken(credential, password);
        copyDetails(token, request);

        return getAuthenticationManager().authenticate(token);
    }

    /**
     * 复制details
     * @param token 令牌
     * @param request 请求
     */
    private void copyDetails(PasswordToken token, HttpServletRequest request) {
        token.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
