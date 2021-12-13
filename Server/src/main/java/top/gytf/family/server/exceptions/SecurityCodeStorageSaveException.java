package top.gytf.family.server.exceptions;

import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码存储保存错误<br>
 * CreateDate:  2021/11/27 23:06 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.STORAGE_SAVE_SECURITY_CODE)
public class SecurityCodeStorageSaveException extends SecurityCodeStorageException {
    private final static String TAG = SecurityCodeStorageSaveException.class.getName();

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SecurityCodeStorageSaveException(String message) {
        super(message);
    }
}
