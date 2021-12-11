package top.gytf.family.server.exceptions;

import org.springframework.security.core.AuthenticationException;
import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: <br>
 * CreateDate:  2021/12/12 2:23 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.USER_LOGIN_ERROR)
public class PasswordUnmatchedErrorException extends AuthenticationException {
    private final static String TAG = PasswordUnmatchedErrorException.class.getName();

    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public PasswordUnmatchedErrorException(String msg) {
        super(msg);
    }
}
