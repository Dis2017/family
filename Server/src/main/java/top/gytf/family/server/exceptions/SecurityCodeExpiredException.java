package top.gytf.family.server.exceptions;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   SecurityCodeExpiredException
 * Description: 验证码过期错误
 * CreateDate:  2021/11/26 23:19
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class SecurityCodeExpiredException extends SecurityCodeException {
    private final static String TAG = SecurityCodeExpiredException.class.getName();

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SecurityCodeExpiredException(String message) {
        super(message);
    }
}
