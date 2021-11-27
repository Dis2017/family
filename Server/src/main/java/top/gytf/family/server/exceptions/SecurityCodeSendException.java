package top.gytf.family.server.exceptions;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码发送错误<br>
 * CreateDate:  2021/11/27 22:55 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class SecurityCodeSendException extends SecurityCodeException {
    private final static String TAG = SecurityCodeSendException.class.getName();

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SecurityCodeSendException(String message) {
        super(message);
    }
}
