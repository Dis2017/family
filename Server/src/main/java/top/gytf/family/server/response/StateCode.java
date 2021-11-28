package top.gytf.family.server.response;

import lombok.Getter;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 状态码<br>
 * CreateDate:  2021/11/28 21:20 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Getter
public enum StateCode {
    /**
     * 基础
     */
    SUCCESS(200,"成功"),
    FAIL(500,"失败"),

    /**
     *  错误参数
     */
    PARAM_IS_INVALID(501,"参数无效"),
    PARAM_IS_BLANK(502,"参为空"),
    PARAM_TYPE_ERROR(503,"参数类型错误"),
    PARAM_NOT_COMPLETE(504,"参数缺失"),

    /**
     *  用户错误
     */
    USER_NOT_LOGIN_IN(601,"用户未登录"),
    USER_LOGIN_ERROR(602,"账号不存在或者密码错误"),
    USER_ACCOUNT_FORBIDDEN(603,"账户被禁用"),
    USER_NOT_EXISTS(604,"用户不存在"),
    USER_HAS_EXISTED(605,"用户已存在"),

    /**
     * 权限错误
     */
    SECURITY_NO_PERMISSION(700, "无权限");

    /**
     *  代码
     */
    private final Integer code;
    /**
     *  提示信息
     */
    private final String message;

    StateCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
