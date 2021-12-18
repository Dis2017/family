package top.gytf.family.server.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 响应切面<br>
 * CreateDate:  2021/11/28 23:57 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@RestControllerAdvice(basePackages = {"top.gytf.family.server.controllers"})
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper mapper;

    public GlobalResponseHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Whether this component supports the given controller method return type
     * and the selected {@code HttpMessageConverter} type.
     *
     * @param returnType    the return type
     * @param converterType the selected converter type
     * @return {@code true} if {@link #beforeBodyWrite} should be invoked;
     * {@code false} otherwise
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查注解是否存在，存在则忽略拦截
        if (returnType.getDeclaringClass().isAnnotationPresent(IgnoreResultAdvice.class)) {
            return false;
        }
        return !Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(IgnoreResultAdvice.class);
    }

    /**
     * Invoked after an {@code HttpMessageConverter} is selected and just before
     * its write method is invoked.
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     * @return the body that was passed in or a modified (possibly new) instance
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        return process(body, returnType.getMethodAnnotation(StatusCarrier.class));
    }

    /**
     * 处理成统一格式
     * @param body 对象
     * @param code 状态码
     * @return 结果
     */
    public Object process(Object body, StateCode code) {
        // 判断是ResponseData子类或其本身就返回Object o本身，因为有可能是接口返回时创建了ResponseData,这里避免再次封装
        if (body instanceof Response) {
            return body;
        }

        // String特殊处理，否则会抛异常
        if (body instanceof String) {
            try {
                return mapper.writeValueAsString(new Response<>(code, body));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new Response<>(StateCode.FAIL, e.getMessage());
            }
        }

        return new Response<>(code, body);
    }

    /**
     * 处理成统一格式
     * @param body 对象
     * @param carrier 状态码携带器
     * @return 结果
     */
    public Object process(Object body, StatusCarrier carrier) {
        return process(body, carrier == null ? StateCode.SUCCESS : carrier.code());
    }

    /**
     * 处理成统一格式
     * @param body 对象
     * @return 结果
     */
    public Object process(Object body) {
        return process(body, StateCode.SUCCESS);
    }
}
