package top.gytf.family.server.security.login;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMethod;
import top.gytf.family.server.security.login.email.EmailAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 认证器<br>
 * CreateDate:  2021/12/15 23:41 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface Authenticator {
    String pattern();
    RequestMethod method();
    Authentication authentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException;
}
