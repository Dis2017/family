package top.gytf.family.server.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.gytf.family.server.utils.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   GlobalExceptionHandler
 * Description  统一异常处理
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 * @date 2021/7/22 15:28
 */

@RestControllerAdvice
public class GlobalExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {
    private final static String TAG = GlobalExceptionHandler.class.getName();

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(Exception.class)
    public Response<String> exceptionHandler(Exception e) {
        if (e instanceof InsufficientAuthenticationException) {
            e = new top.gytf.family.server.exceptions.AccessDeniedException();
        }

        e.printStackTrace();
        StatusCarrier statusCarrier;
        if ((statusCarrier = e.getClass().getAnnotation(StatusCarrier.class)) != null) {
            return new Response<>(statusCarrier.code(), e.getMessage());
        }

        if (e instanceof BindException) {
            //参数校验错误
            BindException bindException = (BindException) e;
            String[] errors = new String[bindException.getErrorCount()];
            List<ObjectError> errorList = bindException.getAllErrors();
            int idx = 0;
            for (ObjectError objectError : errorList) {
                errors[idx++] = objectError.getDefaultMessage();
            }
            return new Response<>(StateCode.PARAM_IS_INVALID, Arrays.toString(errors));
        }  else if (e instanceof IllegalArgumentException) {
            //也是参数错误
            return new Response<>(StateCode.PARAM_IS_INVALID, e.getMessage());
        }

        return new Response<>(StateCode.FAIL, e.getMessage());
    }

    /**
     * Commences an authentication scheme.
     * <p>
     * <code>ExceptionTranslationFilter</code> will populate the <code>HttpSession</code>
     * attribute named
     * <code>AbstractAuthenticationProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY</code>
     * with the requested target URL before calling this method.
     * <p>
     * Implementations should modify the headers on the <code>ServletResponse</code> as
     * necessary to commence the authentication process.
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseUtil.setToJson(response, objectMapper.writeValueAsString(exceptionHandler(authException)));
    }

    /**
     * Handles an access denied failure.
     *
     * @param request               that resulted in an <code>AccessDeniedException</code>
     * @param response              so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     * @throws IOException      in the event of an IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ResponseUtil.setToJson(response, objectMapper.writeValueAsString(exceptionHandler(accessDeniedException)));
    }
}
