package top.gytf.family.server.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.response.GlobalExceptionHandler;
import top.gytf.family.server.response.GlobalResponseHandler;
import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.utils.ResponseUtil;

import javax.servlet.ServletResponse;
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
    private final GlobalExceptionHandler globalExceptionHandler;
    private final GlobalResponseHandler globalResponseHandler;

    public LoginHandler(ObjectMapper objectMapper, GlobalExceptionHandler globalExceptionHandler, GlobalResponseHandler globalResponseHandler) {
        this.objectMapper = objectMapper;
        this.globalExceptionHandler = globalExceptionHandler;
        this.globalResponseHandler = globalResponseHandler;
    }

    /**
     * 写入响应
     * @param response 响应
     * @param obj 对象
     * @throws IOException 写入错误
     */
    private void write(ServletResponse response, Object obj) throws IOException {
        ResponseUtil.setToJson(response,
                objectMapper.writeValueAsString(obj)
        );
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        User user = (User) authentication.getPrincipal();
        user.setPassword(null);
        write(response, globalResponseHandler.process(user));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        Object rep;

        if (exception instanceof UsernameNotFoundException) {
            rep = globalExceptionHandler.process(exception, StateCode.USER_NOT_EXISTS);
        } else {
            rep = globalExceptionHandler.exceptionHandler(exception);
        }

        write(response, rep);
    }
}
