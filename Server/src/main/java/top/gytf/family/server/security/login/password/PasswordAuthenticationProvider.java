package top.gytf.family.server.security.login.password;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import top.gytf.family.server.exceptions.PasswordUnmatchedErrorException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 密码认证提供器<br>
 * CreateDate:  2021/12/1 19:28 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    /**
     * 读取用户信息服务
     */
    private final PasswordUserDetailsServiceImpl passwordUserDetailsService;
    @Setter
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public PasswordAuthenticationProvider(PasswordUserDetailsServiceImpl passwordUserDetailsService) {
        this.passwordUserDetailsService = passwordUserDetailsService;
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
        PasswordToken tokenAuthentication = (PasswordToken) authentication;

        UserDetails userDetails = passwordUserDetailsService.loadUserByUsername(String.valueOf(tokenAuthentication.getPrincipal()));
        if (!(tokenAuthentication.getCredentials() instanceof String) || !passwordEncoder.matches((String) tokenAuthentication.getCredentials(), userDetails.getPassword())) {
            throw new PasswordUnmatchedErrorException("密码错误");
        }

        PasswordToken token = new PasswordToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        token.setDetails(tokenAuthentication.getDetails());

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
     * @param authentication authentication
     * @return <code>true</code> if the implementation can more closely evaluate the
     * <code>Authentication</code> class presented
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordToken.class.isAssignableFrom(authentication);
    }
}
