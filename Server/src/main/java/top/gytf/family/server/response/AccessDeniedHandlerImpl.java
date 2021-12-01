package top.gytf.family.server.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.gytf.family.server.Utils;

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
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private final static String TAG = AccessDeniedHandlerImpl.class.getName();

    private final ObjectMapper objectMapper;

    public AccessDeniedHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        Utils.Response.setToJson(response,
                objectMapper.writeValueAsString(new Response<>(StateCode.SECURITY_NO_PERMISSION,
                        accessDeniedException.getMessage())));
    }
}
