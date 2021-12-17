package top.gytf.family.server.exceptions.curd;

import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 插入错误<br>
 * CreateDate:  2021/12/17 22:37 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.INSERT_EXCEPTION)
public class InsertException extends RuntimeException {
    private final static String TAG = InsertException.class.getName();
}
