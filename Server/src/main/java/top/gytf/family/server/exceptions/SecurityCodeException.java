package top.gytf.family.server.exceptions;

import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   SecurityCodeException
 * Description: 验证码错误
 * CreateDate:  2021/11/26 22:35
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.SECURITY_CODE_EXCEPTION)
public class SecurityCodeException extends RuntimeException {
    private final static String TAG = SecurityCodeException.class.getName();

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SecurityCodeException(String message) {
        super(message);
    }
}
