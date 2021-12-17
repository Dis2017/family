package top.gytf.family.server.exceptions.code;

import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码从存储器中取出错误<br>
 * CreateDate:  2021/11/27 23:03 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.STORAGE_TAKE_SECURITY_CODE)
public class SecurityCodeStorageTakeException extends SecurityCodeStorageException {
    private final static String TAG = SecurityCodeStorageTakeException.class.getName();

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SecurityCodeStorageTakeException(String message) {
        super(message);
    }
}
