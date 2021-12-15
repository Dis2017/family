package top.gytf.family.server.security.login.email;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.security.login.Authenticator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 邮箱认证器<br>
 * CreateDate:  2021/12/15 23:51 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class EmailAuthenticator implements Authenticator {
    private final static String TAG = EmailAuthenticator.class.getName();

    @Override
    public String pattern() {
        return PathConstant.Auth.AUTH_PREFIX + PathConstant.Auth.PATH_EMAIL_LOGIN;
    }

    @Override
    public RequestMethod method() {
        return RequestMethod.POST;
    }

    @Override
    public Authentication authentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return null;
    }

    /**
     * 取出request中的电子邮箱地址
     * @param request 请求
     * @return 电子邮箱地址
     */
    private String getEmail(HttpServletRequest request) {
        return request.getParameter("email");
    }
}
