package top.gytf.family.server.exceptions.curd;

import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 删除错误<br>
 * CreateDate:  2021/12/17 22:37 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.DELETE_EXCEPTION)
public class DeleteException extends RuntimeException {
    private final static String TAG = DeleteException.class.getName();
}
