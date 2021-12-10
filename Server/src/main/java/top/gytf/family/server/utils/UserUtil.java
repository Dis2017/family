package top.gytf.family.server.utils;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户工具<br>
 * CreateDate:  2021/12/10 15:11 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class UserUtil {
    private final static String TAG = UserUtil.class.getName();

    /**
     * 清除保护数据<br>
     * <li>密码</li>
     * <li>邮箱</li>
     * <li>手机号</li>
     * <li>家庭id</li>
     * @param user 用户
     * @param clearPsd 是否清除密码
     */
    public static void clearProtectedMessage(top.gytf.family.server.entity.User user, boolean clearPsd) {
        if (clearPsd) {
            user.setPassword(null);
        }
        user.setEmail(null);
        user.setPhone(null);
        user.setFamilyId(null);
    }
}
