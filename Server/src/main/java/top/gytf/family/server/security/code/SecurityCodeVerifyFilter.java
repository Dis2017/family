package top.gytf.family.server.security.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.gytf.family.server.exceptions.code.SecurityCodeException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * 匹配器<br>
     * 用于匹配url
     */
    private final AntPathMatcher matcher = new AntPathMatcher();
    private final Map<Class<? extends SecurityCodeRequestValidator>, SecurityCodeRequestValidator> validators;
    private final Map<RequestInfo, SecurityCodeVerifyStrategyInfo> urlVerifyStrategyMap;

    protected SecurityCodeVerifyFilter(ApplicationContext context) {
        validators = new HashMap<>();
        context.getBeansOfType(SecurityCodeRequestValidator.class).values()
                .forEach((validator) -> validators.put(validator.getClass(), validator));
        urlVerifyStrategyMap = getUrlVerifyStrategyMap(context);
    }

    /**
     * 获取url和验证策略的映射
     * @param context 上下文
     * @return 映射
     */
    private Map<RequestInfo, SecurityCodeVerifyStrategyInfo> getUrlVerifyStrategyMap(ApplicationContext context) {
        Map<RequestInfo, SecurityCodeVerifyStrategyInfo> result = new HashMap<>(32);

        // 接口
        RequestMappingHandlerMapping requestMappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            SecurityCodeVerifyStrategy strategy = entry.getValue().getMethodAnnotation(SecurityCodeVerifyStrategy.class);
            if (strategy == null) {
                continue;
            }

            // 产生校验策略实体
            Set<SecurityCodeRequestValidator> validatorSet = Arrays.stream(strategy.value())
                    .map(validators::get)
                    .collect(Collectors.toSet());
            if (validatorSet.contains(null)) {
                throw new RuntimeException(entry.getKey().toString() + "验证器不齐全");
            }
            SecurityCodeVerifyStrategyInfo strategyInfo = new SecurityCodeVerifyStrategyInfo(strategy.only(), validatorSet);

            // 放入
            RequestMappingInfo key = entry.getKey();
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = key.getMethodsCondition().getMethods();
            methods.stream()
                    .map(Enum::name)
                    .forEach((method) -> patterns
                            .forEach((pattern) -> result.put(new RequestInfo(pattern, method), strategyInfo)));
        }

        // 类
        Map<String, Object> beans = context.getBeansWithAnnotation(SecurityCodeVerifyStrategy.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            SecurityCodeVerifyStrategy strategy = entry.getValue().getClass().getAnnotation(SecurityCodeVerifyStrategy.class);

            // 产生校验策略实体
            Set<SecurityCodeRequestValidator> validatorSet = Arrays.stream(strategy.value())
                    .map(validators::get)
                    .collect(Collectors.toSet());
            if (validatorSet.contains(null)) {
                throw new RuntimeException(
                        '[' + Arrays.toString(strategy.patterns()) + ','
                                + Arrays.toString(strategy.methods()) + ']'
                                + "验证器不齐全");
            }
            SecurityCodeVerifyStrategyInfo strategyInfo = new SecurityCodeVerifyStrategyInfo(strategy.only(), validatorSet);

            // 放入
            Stream<String> patterns = Arrays.stream(strategy.patterns());
            RequestMethod[] methods = strategy.methods();
            Arrays.stream(methods)
                    .map(Enum::name)
                    .forEach((method) -> patterns
                            .forEach((pattern) -> result.put(new RequestInfo(pattern, method), strategyInfo)));
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
        String method = request.getMethod();
        RequestInfo requestInfo = new RequestInfo(uri, method);
        SecurityCodeVerifyStrategyInfo strategyInfo = urlVerifyStrategyMap.get(requestInfo);

        if (strategyInfo != null) {
            // 需要验证
            boolean only = strategyInfo.isOnly();
            Collection<? extends SecurityCodeRequestValidator> validators = strategyInfo.getValidators();

            boolean ok = false;
            StringBuilder errorMsg = new StringBuilder();

            for (SecurityCodeRequestValidator validator : validators) {
                try {
                    // 验证
                    validator.verifyRequest(request);
                    ok = true;
                    //只需要验证通过一个
                    if (only) {
                        break;
                    }
                } catch (Exception e) {
                    //有一个没有通过验证并且要求所有都通过验证，无法满足
                    if (!only) {
                        if (e instanceof SecurityCodeException) {
                            throw e;
                        }
                        throw new SecurityCodeException(e.getMessage());
                    } else {
                        errorMsg.append(validator.name()).append(": ").append(e.getMessage()).append('\n');
                    }
                }
            }

            // 一个也没有通过
            if (!ok) {
                throw new SecurityCodeException(errorMsg.toString());
            }
        }

        // 验证通过 或 不需要验证
        filterChain.doFilter(request, response);
    }
}
