package top.gytf.family.server.constants;

import top.gytf.family.server.security.login.email.EmailAuthenticationFilter;
import top.gytf.family.server.security.login.password.PasswordAuthenticationFilter;

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
         * 登出的路径<br>
         * <b><font color="green">不通过Controller提供服务！</font></b><br>
         * <b><font color="green">在</font>{@link top.gytf.family.server.config.security.SecurityConfig}
         * <font color="green">绑定</font></b>
         */
        public static final String PATH_LOGOUT = "/logout";

        /**
         * 用户编号密码登录的路径<br>
         * <b><font color="green">不通过Controller提供服务！</font></b><br>
         * <b><font color="green">在</font>{@link PasswordAuthenticationFilter#PasswordAuthenticationFilter()}
         * <font color="green">绑定</font></b>
         */
        public static final String PATH_PASSWORD_LOGIN = "/login/password";

        /**
         * 邮箱登录的路径<br>
         * <b><font color="green">不通过Controller提供服务！</font></b><br>
         * <b><font color="green">在</font>{@link EmailAuthenticationFilter#EmailAuthenticationFilter()} )}
         * <font color="green">绑定</font></b>
         */
        public static final String PATH_EMAIL_LOGIN = "/login/email";

        /**
         * 邮箱验证码生成路径
         */
        public static final String PATH_SECURITY_CODE_EMAIL = "/security-code/email";

        /**
         * 图片验证码生成路径
         */
        public static final String PATH_SECURITY_CODE_IMAGE = "/security-code/image";
    }

    /**
     * 用户
     */
    public static class User {
        /**
         * 寻找用户
         */
        public static final String PATH_FIND_USER = "";
        /**
         * 寻找用户分页
         */
        public static final String PATH_FIND_USER_PAGE = "/page";
        /**
         * 注册路径
         */
        public static final String PATH_REGISTER = "";
        /**
         * 更新用户信息路径
         */
        public static final String PATH_MODIFY = "";
        /**
         * 用户路径前缀
         */
        public static final String USER_PREFIX = "/user";
        /**
         * 更新密码
         */
        public static final String PATH_MODIFY_PASSWORD = "/password";
        /**
         * 绑定邮箱路径
         */
        public static final String PATH_BIND_EMAIL = "/email";
        /**
         * 解绑邮箱路径
         */
        public static final String PATH_UNBIND_EMAIL = "/email";

        /**
         * 下载头像
         */
        public static final String PATH_DOWNLOAD_AVATAR = "/avatar";

        /**
         * 上传头像
         */
        public static final String PATH_UPLOAD_AVATAR = "/avatar";

    }
}
