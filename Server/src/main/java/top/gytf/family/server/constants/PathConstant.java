package top.gytf.family.server.constants;

import top.gytf.family.server.security.code.SecurityCodeRequestValidator;
import top.gytf.family.server.security.code.SecurityCodeVerifyStrategy;
import top.gytf.family.server.security.code.email.EmailSecurityCodeRequestValidator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   PathConstant
 * Description: 路径常量
 * CreateDate:  2021/11/26 23:49
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class PathConstant {
    private final static String TAG = PathConstant.class.getName();

    /**
     * 认证
     */
    public static class Auth {
        /**
         * 授权路径前缀
         */
        public static final String AUTH_PREFIX = "/auth";

        /**
         * 所有的授权路径<br>
         * 匹配串
         */
        public static final String PATH_ALL_AUTH = AUTH_PREFIX + "/**";

        /**
         * 当前登录用户的路径
         */
        public static final String  PATH_CURRENT = "/current";

        /**
         * 登出的路径
         */
        public static final String PATH_LOGOUT = "/logout";

        /**
         * 用户编号密码登录的路径
         */
        public static final String PATH_ID_PASSWORD_LOGIN = "/login/id";

        /**
         * 邮箱登录的路径
         */
        public static final String PATH_EMAIL_LOGIN = "/login/email";

        /**
         * 邮箱验证码路径
         */
        public static final String PATH_SECURITY_CODE_EMAIL = "/security-code/email";

        /**
         * 图片验证码路径
         */
        public static final String PATH_SECURITY_CODE_IMAGE = "/security-code/image";

        /**
         * 验证密码路径
         */
        public static final String PATH_VERIFY_PASSWORD = "/security-code/password";
    }

    /**
     * 用户
     */
    public static class User {
        /**
         * 用户路径前缀
         */
        public static final String USER_PREFIX = "/user";
        /**
         * 更新密码
         */
        public static final String PATH_MODIFY_PASSWORD = "/password";
        /**
         * 注册路径
         */
        public static final String PATH_REGISTER = "";
        /**
         * 绑定邮箱路径
         */
        public static final String PATH_BIND_EMAIL = "/email";
        /**
         * 解绑邮箱路径
         */
        public static final String PATH_UNBIND_EMAIL = "/email";
    }
}
