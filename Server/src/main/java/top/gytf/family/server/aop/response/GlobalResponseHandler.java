package top.gytf.family.server.aop.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.gytf.family.server.response.Response;
import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.utils.ResponseUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 统一响应处理器<br>
 * 调用{@link top.gytf.family.server.response.GlobalResponseHandler#GlobalResponseHandler}
 * CreateDate:  2021/12/17 21:32 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Aspect
@Component("aop-GlobalResponseHandler")
@AllArgsConstructor
public class GlobalResponseHandler {

    private final ObjectMapper objectMapper;

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
    @AfterReturning("allVoidResponseApi() && !allIgnoreResultAdviceApi()")
    public void setToEmptyGlobalResponse() throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //无法转换类型
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return;
        }

        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        assert response != null : "无法获取Response";

        ResponseUtil.setToJson(
                response,
                objectMapper.writeValueAsString(new Response<>(StateCode.SUCCESS, null))
        );
    }
}
