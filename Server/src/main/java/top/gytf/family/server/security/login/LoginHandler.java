package top.gytf.family.server.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.response.Response;
import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;
import top.gytf.family.server.utils.ResponseUtil;

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
            throws IOException {
        User user = (User) authentication.getPrincipal();
        user.setPassword(null);
        ResponseUtil.setToJson(response,
                objectMapper.writeValueAsString(new Response<>(StateCode.SUCCESS, user)));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        if (exception instanceof UsernameNotFoundException) {
            ResponseUtil.setToJson(response,
                    objectMapper.writeValueAsString(new Response<>(StateCode.USER_NOT_EXISTS, exception.getMessage())));
            return;
        }
        StatusCarrier statusCarrier = exception.getClass().getAnnotation(StatusCarrier.class);
        StateCode stateCode = StateCode.FAIL;

        if (statusCarrier != null) {
            stateCode = statusCarrier.code();
        }

        ResponseUtil.setToJson(response,
                objectMapper.writeValueAsString(new Response<>(stateCode, exception.getMessage()))
        );
    }
}
