package top.gytf.family.server.constants;

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
     * 所有的授权路径<br>
     * 匹配串
     */
    public static final String PATH_ALL_AUTH = "/auth/**";

    /**
     * 邮箱登录的路径<br>
     */
    public static final String PATH_EMAIL_LOGIN = "/auth/login/email";
}
