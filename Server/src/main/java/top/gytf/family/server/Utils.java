package top.gytf.family.server;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.security.email.EmailAuthenticationToken;
import top.gytf.family.server.security.id.IdPasswordToken;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 工具包<br>
 * CreateDate:  2021/11/30 16:03 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class Utils {
    private final static String TAG = Utils.class.getName();

    /**
     * 请求
     */
    public static class Response {
        /**
         * 设置成Json响应
         * @param response 响应体
         * @param json json内容
         * @throws IOException 写入失败
         */
        public static void setToJson(HttpServletResponse response, String json) throws IOException {
            //设置响应头
            response.reset();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        response.setContentType("application/json;charset=UTF-8");
            //设置JSON
            response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        }

        /**
         * 设置成下载响应
         * @param response 响应体
         * @param file 文件
         * @throws IOException 下载失败
         */
        public static void setToDownload(HttpServletResponse response, File file) throws IOException {
            //设置响应头
            response.reset();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            //设置文件
            try (FileInputStream inputStream = new FileInputStream(file)) {
                OutputStream out = response.getOutputStream();
                byte[] bytes = new byte[1024];
                while (inputStream.read(bytes) > 0) {
                    out.write(bytes);
                }
            }
        }
    }

    /**
     * 用户
     */
    public static class User {
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
            if (clearPsd) user.setPassword(null);
            user.setEmail(null);
            user.setPhone(null);
            user.setFamilyId(null);
        }
    }

    /**
     * 安全
     */
    public static class Security {
        /**
         * 当前登录用户
         * @return 用户
         */
        public static top.gytf.family.server.entity.User current() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principalObj = null;
            if (authentication != null) principalObj = authentication.getPrincipal();
            if (!(principalObj instanceof top.gytf.family.server.entity.User)) return null;
            return (top.gytf.family.server.entity.User) principalObj;
        }

        /**
         * 更新当前登录用户
         * @param user 用户
         */
        public static void update(top.gytf.family.server.entity.User user) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof EmailAuthenticationToken) {
                EmailAuthenticationToken newToken = new EmailAuthenticationToken(
                        user,
                        authentication.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(newToken);
            } else if (authentication instanceof IdPasswordToken) {
                IdPasswordToken newToken = new IdPasswordToken(
                        user,
                        authentication.getCredentials(),
                        authentication.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(newToken);
            }
        }
    }
}
