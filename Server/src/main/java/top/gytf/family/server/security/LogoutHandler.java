package top.gytf.family.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import top.gytf.family.server.response.Response;
import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.utils.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   LogoutHandler
 * Description  登出处理器
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 * @date 2021/8/7 14:00
 */
@Component
public class LogoutHandler implements LogoutSuccessHandler {
    private final static String TAG = LogoutHandler.class.getName();

    private final ObjectMapper objectMapper;

    public LogoutHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        ResponseUtil.setToJson(response, objectMapper.writeValueAsString(new Response<>(StateCode.SUCCESS, "成功登出")));
    }
}
