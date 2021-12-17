package top.gytf.family.server.exceptions;

import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

import java.security.PrivilegedActionException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 非法参数错误<br>
 * CreateDate:  2021/12/17 16:08 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.PARAM_IS_INVALID)
public class IllegalArgumentException extends java.lang.IllegalArgumentException {
    private final static String TAG = IllegalArgumentException.class.getName();

    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public IllegalArgumentException() {
    }

    /**
     * Constructs an {@code IllegalArgumentException} with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public IllegalArgumentException(String s) {
        super(s);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method).  (A {@code null} value
     *                is permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.5
     */
    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of {@code (cause==null ? null : cause.toString())} (which
     * typically contains the class and detail message of {@code cause}).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link Throwable#getCause()} method).  (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.5
     */
    public IllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
