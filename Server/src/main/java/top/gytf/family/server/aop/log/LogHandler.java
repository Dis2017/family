package top.gytf.family.server.aop.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 日志处理器<br>
 * CreateDate:  2021/12/16 22:40 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
@Aspect
@Slf4j
public class LogHandler {
    private final static String TAG = LogHandler.class.getName();

    /**
     * 所有api
     */
    @Pointcut("execution(public * top.gytf.family.server.controllers..*(..))")
    public void allApi() {}

    /**
     * 所有api执行前调用
     */
    @Before("allApi()")
    public void handler() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        log.debug(requestAttributes.getSessionId());
    }
}
