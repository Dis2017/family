package top.gytf.family.server.security.email;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   EmailAuthenticationProvider
 * Description: 邮箱认证器
 * CreateDate:  2021/11/25 22:22
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class EmailAuthenticationProvider implements AuthenticationProvider {
    private final static String TAG = EmailAuthenticationProvider.class.getName();

    /**
     * 读取用户信息服务
     */
    private final EmailUserDetailsServiceImpl emailUserDetailsService;

    public EmailAuthenticationProvider(EmailUserDetailsServiceImpl emailUserDetailsService) {
        this.emailUserDetailsService = emailUserDetailsService;
    }

    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)}
     * .
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return
     * <code>null</code> if the <code>AuthenticationProvider</code> is unable to support
     * authentication of the passed <code>Authentication</code> object. In such a case,
     * the next <code>AuthenticationProvider</code> that supports the presented
     * <code>Authentication</code> class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailAuthenticationToken emailAuthentication = (EmailAuthenticationToken) authentication;

        String email = emailAuthentication.getName();
        UserDetails userDetails = emailUserDetailsService.loadUserByUsername(email);
        EmailAuthenticationToken token = new EmailAuthenticationToken(userDetails, userDetails.getAuthorities());
        token.setDetails(authentication.getDetails());

        return token;
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the
     * indicated <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the presented
     * instance of the <code>Authentication</code> class. It simply indicates it can
     * support closer evaluation of it. An <code>AuthenticationProvider</code> can still
     * return <code>null</code> from the {@link #authenticate(Authentication)} method to
     * indicate another <code>AuthenticationProvider</code> should be tried.
     * </p>
     * <p>
     * Selection of an <code>AuthenticationProvider</code> capable of performing
     * authentication is conducted at runtime the <code>ProviderManager</code>.
     * </p>
     *
     * @param authentication 认证内容
     * @return <code>true</code> if the implementation can more closely evaluate the
     * <code>Authentication</code> class presented
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
