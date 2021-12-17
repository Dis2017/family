package top.gytf.family.server.response;

import lombok.Data;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 响应体<br>
 * CreateDate:  2021/11/28 21:24 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Data
public class Response<T> {
    private final static String TAG = Response.class.getName();

    /**
     * 状态码
     */
    private final int code;
    /**
     * 消息
     */
    private final String msg;
    /**
     * 数据
     */
    private final T data;

    public Response(StateCode stateCode, T data) {
        this.code = stateCode.getCode();
        this.msg = stateCode.getMessage();
        this.data = data;
    }
}
