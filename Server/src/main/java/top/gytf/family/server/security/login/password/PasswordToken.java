package top.gytf.family.server.security.login.password;

import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import top.gytf.family.server.exceptions.IllegalArgumentException;

import java.util.Collection;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 编号密码令牌<br>
 * CreateDate:  2021/12/1 19:18 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class PasswordToken extends AbstractAuthenticationToken {
    private final static String TAG = PasswordToken.class.getName();

    @Setter
    private Object principal;

    @Setter
    private Object credentials;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     * @param id   编号
     * @param password 密码
     */
    public PasswordToken(Object id, Object password, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = id;
        this.credentials = password;
        super.setAuthenticated(true);
    }

    public PasswordToken(String id, String password) {
        this(id, password, null);
        setAuthenticated(false);
    }

    /**
     * 设置是否已经认证<br>
     * 不要直接设置Token为已认证，请重新创建带权限列表的Token。
     *
     * @param authenticated 是否授权，为true将抛出异常
     */
    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("不要直接设置Token为已认证，请重新创建带权限列表的Token。");
        }
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
        return credentials;
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
        return principal;
    }
}
