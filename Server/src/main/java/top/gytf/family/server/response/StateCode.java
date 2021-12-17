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
     * 未找到访问资源
     */
    NOT_FOUND(404,"未找到访问资源"),

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
    USER_NOT_LOGIN(601,"用户未登录"),
    USER_LOGIN_ERROR(602,"密码错误"),
    USER_ACCOUNT_FORBIDDEN(603,"账户被禁用"),
    USER_NOT_EXISTS(604,"用户不存在"),
    USER_HAS_EXISTED(605,"用户已存在"),
    USER_REPEAT_LOGIN(606,"重复登录"),
    AVATAR_NOT_FOUND(607, "头像未找到"),

    /**
     * 权限错误
     */
    SECURITY_NO_PERMISSION(700, "无权限"),

    /**
     * 验证码错误
     */
    SECURITY_CODE_EXCEPTION(800, "验证码错误"),
    NULL_SECURITY_CODE(801, "验证码为空"),
    EXPIRED_SECURITY_CODE(802, "验证码已过期"),
    NOT_MATCH_SECURITY_CODE(803, "验证码不匹配"),
    GENERATE_SECURITY_CODE(804, "验证码生成失败"),
    SEND_SECURITY_CODE(805, "验证码发送失败"),
    STORAGE_TAKE_SECURITY_CODE(806, "验证码取出失败"),
    STORAGE_SAVE_SECURITY_CODE(807, "验证码存储失败"),
    STORAGE_REMOVE_SECURITY_CODE(808, "验证码移除失败");

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
