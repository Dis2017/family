package top.gytf.family.server.exceptions;

import org.springframework.security.core.AuthenticationException;
import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 未登录错误<br>
 * CreateDate:  2021/12/11 0:57 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(StateCode.USER_NOT_LOGIN_IN)
public class NotLoginException extends AuthenticationException {
    private final static String TAG = NotLoginException.class.getName();

    public NotLoginException() {
        super("未登录");
    }
}
