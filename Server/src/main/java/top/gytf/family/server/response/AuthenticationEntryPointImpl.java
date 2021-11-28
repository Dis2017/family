package top.gytf.family.server.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.gytf.family.server.utills.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   AuthenticationEntryPoint
 * Description  处理匿名用户
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 * @date 2021/8/7 11:04
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final static String TAG = AuthenticationEntryPointImpl.class.getName();

    private final ObjectMapper objectMapper;

    public AuthenticationEntryPointImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ResponseUtil.setToJson(response, objectMapper.writeValueAsString(new Response<>(StateCode.USER_NOT_LOGIN_IN, authException.getMessage())));
    }
}
