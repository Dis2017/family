package top.gytf.family.server.security.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import top.gytf.family.server.Utils;
import top.gytf.family.server.exceptions.SecurityCodeException;
import top.gytf.family.server.response.Response;
import top.gytf.family.server.response.StateCode;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码失败处理器<br>
 * CreateDate:  2021/12/5 17:50 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class SecurityCodeVerifyFailureHandler {
    private final ObjectMapper mapper;

    public SecurityCodeVerifyFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 失败时调用<br>
     * 在{@link SecurityCodeVerifyFilter#doFilterInternal}调用
     * @param request 请求
     * @param response 响应
     * @param exception 错误
     */
    public void onFailure(HttpServletRequest request, HttpServletResponse response, SecurityCodeException exception) throws IOException {
        Utils.Response.setToJson(response,
                mapper.writeValueAsString(new Response<>(StateCode.SECURITY_CODE_EXCEPTION, exception.getMessage())));
    }
}
