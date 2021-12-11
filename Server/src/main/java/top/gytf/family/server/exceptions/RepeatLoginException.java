package top.gytf.family.server.exceptions;

import org.springframework.security.core.AuthenticationException;
import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 重复登录错误<br>
 * CreateDate:  2021/12/12 2:12 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.USER_REPEAT_LOGIN)
public class RepeatLoginException extends AuthenticationException {
    private final static String TAG = RepeatLoginException.class.getName();

    public RepeatLoginException() {
        super("重复登录");
    }
}
