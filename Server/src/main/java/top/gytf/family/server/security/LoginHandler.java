package top.gytf.family.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.gytf.family.server.response.Response;
import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.utills.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   LoginSuccessHandler
 * Description  登录处理器
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 * @date 2021/8/7 11:53
 */
@Component
public class LoginHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {
    private final static String TAG = LoginHandler.class.getName();

    private final ObjectMapper objectMapper;

    public LoginHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        ResponseUtil.setToJson(response,
                objectMapper.writeValueAsString(new Response<>(StateCode.SUCCESS, authentication.getPrincipal())));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
//        if (exception instanceof UsernameNotFoundException) {
//            ResponseUtil.setToJson(response,
//                    objectMapper.writeValueAsString(new Response<>(StatusCode.NOT_FOUND, exception.getMessage())));
//            return;
//        }
        ResponseUtil.setToJson(response,
                objectMapper.writeValueAsString(new Response<>(StateCode.FAIL, exception.getMessage()))
        );
    }
}
