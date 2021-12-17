package top.gytf.family.server.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.filter.GenericFilterBean;
import top.gytf.family.server.utils.ResponseUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
public class GlobalExceptionHandler extends GenericFilterBean {
    private final static String TAG = GlobalExceptionHandler.class.getName();

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 处理成统一响应
     * @param e 错误
     * @param code 状态
     * @return 结果
     */
    public Response<String> process(Exception e, StateCode code) {
        e.printStackTrace();

        if (code == null || code == StateCode.FAIL) {
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
            }
        }

        if (code == null) {
            code = StateCode.FAIL;
        }

        return new Response<>(code, e.getMessage());
    }

    /**
     * 处理成统一响应
     * @param e 错误
     * @param carrier 状态携带器
     * @return 结果
     */
    public Response<String> process(Exception e, StatusCarrier carrier) {
        return process(e, carrier == null ? StateCode.FAIL : carrier.code());
    }

    @ExceptionHandler(Exception.class)
    public Response<String> exceptionHandler(Exception e) {
        return process(e, e.getClass().getAnnotation(StatusCarrier.class));
    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the container
     * each time a request/response pair is passed through the chain due to a
     * client request for a resource at the end of the chain. The FilterChain
     * passed in to this method allows the Filter to pass on the request and
     * response to the next entity in the chain.
     * <p>
     * A typical implementation of this method would follow the following
     * pattern:- <br>
     * 1. Examine the request<br>
     * 2. Optionally wrap the request object with a custom implementation to
     * filter content or headers for input filtering <br>
     * 3. Optionally wrap the response object with a custom implementation to
     * filter content or headers for output filtering <br>
     * 4. a) <strong>Either</strong> invoke the next entity in the chain using
     * the FilterChain object (<code>chain.doFilter()</code>), <br>
     * 4. b) <strong>or</strong> not pass on the request/response pair to the
     * next entity in the filter chain to block the request processing<br>
     * 5. Directly set headers on the response after invocation of the next
     * entity in the filter chain.
     *
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this
     *                 filter to pass the request and response to for further
     *                 processing
     * @throws IOException      if an I/O error occurs during this filter's
     *                          processing of the request
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            ResponseUtil.setToJson(response, objectMapper.writeValueAsString(exceptionHandler(e)));
        }
    }
}
