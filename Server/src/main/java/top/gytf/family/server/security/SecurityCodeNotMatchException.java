package top.gytf.family.server.security;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   SecurityCodeNotMatchException
 * Description: 验证码不匹配错误
 * CreateDate:  2021/11/26 23:22
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class SecurityCodeNotMatchException extends SecurityCodeException{
    private final static String TAG = SecurityCodeNotMatchException.class.getName();

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SecurityCodeNotMatchException(String message) {
        super(message);
    }
}
