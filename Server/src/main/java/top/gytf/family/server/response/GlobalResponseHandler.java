package top.gytf.family.server.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.gytf.family.server.utils.ResponseUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@RestControllerAdvice(basePackages = {"top.gytf.family.server.controller"})
@Aspect
@Slf4j
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper mapper;

    public GlobalResponseHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 指向{@link top.gytf.family.server.controllers}包下的所有返回值为void的接口
     */
    @Pointcut("execution(public void top.gytf.family.server.controllers..*(..))")
    public void allVoidResponseApi() {}

    /**
     * 指向所有标注了{@link IgnoreResultAdvice}的接口
     */
    @Pointcut("@annotation(IgnoreResultAdvice)")
    public void allIgnoreResultAdviceApi() {}

    /**
     * 设置为空的全局响应<br>
     * 在调用相关接口({@link #allVoidResponseApi()}并且不是{@link #allIgnoreResultAdviceApi()} ()}所指向的连接点)之前执行
     * @throws IOException 写入响应错误
     */
    @After("allVoidResponseApi() && !allIgnoreResultAdviceApi()")
    public void setToEmptyGlobalResponse() throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //无法转换类型
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return;
        }

        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        assert response != null : "无法获取Response";
        ResponseUtil.setToJson(response, mapper.writeValueAsString(new Response<>(StateCode.SUCCESS, null)));
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
        // 判断是ResponseData子类或其本身就返回Object o本身，因为有可能是接口返回时创建了ResponseData,这里避免再次封装
        if (body instanceof Response) {
            return body;
        }
        StatusCarrier statusCarrier;
        if ((statusCarrier = returnType.getMethodAnnotation(StatusCarrier.class)) != null) {
            return new Response<>(statusCarrier.value(), body);
        }
        // String特殊处理，否则会抛异常
        if (body instanceof String) {
            try {
                return mapper.writeValueAsString(new Response<>(StateCode.SUCCESS, body));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new Response<>(StateCode.FAIL, e.getMessage());
            }
        }

        return new Response<>(StateCode.SUCCESS, body);
    }
}
