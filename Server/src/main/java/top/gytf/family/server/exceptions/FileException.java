package top.gytf.family.server.exceptions;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 文件错误<br>
 * CreateDate:  2021/12/13 18:11 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class FileException extends RuntimeException{
    private final static String TAG = FileException.class.getName();

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public FileException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public FileException(String message) {
        super(message);
    }
}
