package top.gytf.family.server.security.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.gytf.family.server.exceptions.SecurityCodeException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码验证过滤器<br>
 * CreateDate:  2021/11/29 23:24 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
@Slf4j
public class SecurityCodeVerifyFilter extends OncePerRequestFilter {
    private final static String TAG = SecurityCodeVerifyFilter.class.getName();

    private final AntPathMatcher matcher = new AntPathMatcher();
    private final Map<Class<? extends SecurityCodeRequestValidator>, SecurityCodeRequestValidator> validators;
    private final Map<String, SecurityCodeVerifyStrategy> urlVerifyStrategyMap;
    private final SecurityCodeVerifyFailureHandler failureHandler;

    protected SecurityCodeVerifyFilter(ApplicationContext context, SecurityCodeVerifyFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        validators = new HashMap<>();
        context.getBeansOfType(SecurityCodeRequestValidator.class).values().forEach((validator) -> validators.put(validator.getClass(), validator));
        urlVerifyStrategyMap = getUrlVerifyStrategyMap(context);
    }

    /**
     * 获取url和验证策略的映射
     * @param context 上下文
     * @return 映射
     */
    private Map<String, SecurityCodeVerifyStrategy> getUrlVerifyStrategyMap(ApplicationContext context) {
        Map<String, SecurityCodeVerifyStrategy> result = new HashMap<>();

        // 接口
        RequestMappingHandlerMapping requestMappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            SecurityCodeVerifyStrategy strategy = entry.getValue().getMethodAnnotation(SecurityCodeVerifyStrategy.class);
            if (strategy == null) continue;
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            patterns.forEach((pattern) -> result.put(pattern, strategy));
        }

        // 类
        Map<String, Object> beans = context.getBeansWithAnnotation(SecurityCodeVerifyStrategy.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            SecurityCodeVerifyStrategy strategy = entry.getValue().getClass().getAnnotation(SecurityCodeVerifyStrategy.class);
            String[] patterns = strategy.patterns();
            Arrays.stream(patterns).forEach((pattern) -> result.put(pattern, strategy));
        }

        return result;
    }

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request request
     * @param response response
     * @param filterChain filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        for (Map.Entry<String, SecurityCodeVerifyStrategy> entry : urlVerifyStrategyMap.entrySet()) {
            if (!matcher.match(entry.getKey(), uri)) continue;

            Class<? extends SecurityCodeRequestValidator<?, ?>>[] validatorClasses = entry.getValue().value();
            boolean only = entry.getValue().only();
            boolean ok = false;
            StringBuilder errorMsg = new StringBuilder();

            for (Class<? extends SecurityCodeRequestValidator<?, ?>> validatorClass : validatorClasses) {
                SecurityCodeRequestValidator validator = validators.get(validatorClass);
                try {
                    validator.verifyRequest(request);
                    ok = true;
                    //只需要验证通过一个
                    if (only) break;
                } catch (SecurityCodeException e) {
                    //有一个没有通过验证并且要求所有都通过验证，无法满足
                    if (!only) {
                        e.printStackTrace();
                        failureHandler.onFailure(request, response, e);
                        return;
                    } else errorMsg.append(validator.name()).append(": ").append(e.getMessage());
                }
            }

            // 一个也没有通过
            if (!ok) {
                failureHandler.onFailure(request, response, new SecurityCodeException(errorMsg.toString()));
                return;
            }

            break;
        }

        filterChain.doFilter(request, response);
    }
}
