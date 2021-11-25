package top.gytf.family.server.security.email;

import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   EmailAuthenticationToken
 * Description: 邮箱授权令牌
 * CreateDate:  2021/11/25 15:56
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class EmailAuthenticationToken extends AbstractAuthenticationToken {
    private final static String TAG = EmailAuthenticationToken.class.getName();

    @Setter
    private String email;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     *
     * @param email the principal of token
     */
    public EmailAuthenticationToken(String email, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = email == null ? "" : email;
        super.setAuthenticated(true);
    }

    public EmailAuthenticationToken(String email) {
        this(email, null);
        setAuthenticated(false);
    }

    /**
     * 设置是否已经认证<br>
     * 不要直接设置Token为已认证，请重新创建带权限列表的Token。
     * @param authenticated 是否授权，为true将抛出异常
     */
    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) throw new IllegalArgumentException("不要直接设置Token为已认证，请重新创建带权限列表的Token。");
        super.setAuthenticated(false);
    }

    /**
     * The credentials that prove the principal is correct. This is usually a password,
     * but could be anything relevant to the <code>AuthenticationManager</code>. Callers
     * are expected to populate the credentials.
     *
     * @return the credentials that prove the identity of the <code>Principal</code>
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * The identity of the principal being authenticated. In the case of an authentication
     * request with username and password, this would be the username. Callers are
     * expected to populate the principal for an authentication request.
     * <p>
     * The <tt>AuthenticationManager</tt> implementation will often return an
     * <tt>Authentication</tt> containing richer information as the principal for use by
     * the application. Many of the authentication providers will create a
     * {@code UserDetails} object as the principal.
     *
     * @return the <code>Principal</code> being authenticated or the authenticated
     * principal after authentication.
     */
    @Override
    public Object getPrincipal() {
        return email;
    }
}
