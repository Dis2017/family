package top.gytf.family.server.exceptions;

import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 统一查询条件格式错误<br>
 * CreateDate:  2021/12/12 13:35 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.PARAM_IS_INVALID)
public class GeneralSearchConditionFormatException extends RuntimeException{
    private final static String TAG = GeneralSearchConditionFormatException.class.getName();

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public GeneralSearchConditionFormatException() {
        super("格式错误");
    }
}
