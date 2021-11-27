package top.gytf.family.server.exceptions;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码移除错误<br>
 * CreateDate:  2021/11/27 23:09 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class SecurityCodeStorageRemoveException extends SecurityCodeSendException {
    private final static String TAG = SecurityCodeStorageRemoveException.class.getName();

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SecurityCodeStorageRemoveException(String message) {
        super(message);
    }
}
