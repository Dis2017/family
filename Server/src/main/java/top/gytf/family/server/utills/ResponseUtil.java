package top.gytf.family.server.utills;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   ResponseUtil
 * Description  响应工具类
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 * @date 2021/7/31 18:16
 */
public class ResponseUtil {
    private final static String TAG = ResponseUtil.class.getName();

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
