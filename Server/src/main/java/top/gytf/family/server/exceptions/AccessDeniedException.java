package top.gytf.family.server.exceptions;

import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 权限不足<br>
 * CreateDate:  2021/12/12 22:26 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.SECURITY_NO_PERMISSION)
public class AccessDeniedException extends org.springframework.security.access.AccessDeniedException {
    private final static String TAG = AccessDeniedException.class.getName();

    public AccessDeniedException() {
        this("权限不足");
    }

    /**
     * Constructs an <code>AccessDeniedException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public AccessDeniedException(String msg) {
        super(msg);
    }

    /**
     * Constructs an <code>AccessDeniedException</code> with the specified message and
     * root cause.
     *
     * @param msg the detail message
     * @param t   root cause
     */
    public AccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }
}
