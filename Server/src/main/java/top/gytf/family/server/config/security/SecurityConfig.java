package top.gytf.family.server.config.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.response.AccessDeniedHandlerImpl;
import top.gytf.family.server.response.AuthenticationEntryPointImpl;
import top.gytf.family.server.security.LogoutHandler;

import javax.annotation.security.PermitAll;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   SecurityConfig
 * Description: 安全配置
 * CreateDate:  2021/11/24 22:46
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final static String TAG = SecurityConfig.class.getName();

    private final ApplicationContext applicationContext;
    private final SecurityCodeConfig securityCodeConfig;
    private final EmailSecurityConfig emailSecurityConfig;
    private final IdPasswordConfig idPasswordConfig;
    private final LogoutHandler logoutHandler;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;

    public SecurityConfig(ApplicationContext applicationContext, SecurityCodeConfig securityCodeConfig, EmailSecurityConfig emailSecurityConfig, IdPasswordConfig idPasswordConfig, LogoutHandler logoutHandler, AccessDeniedHandlerImpl accessDeniedHandler, AuthenticationEntryPointImpl authenticationEntryPoint) {
        this.applicationContext = applicationContext;
        this.securityCodeConfig = securityCodeConfig;
        this.emailSecurityConfig = emailSecurityConfig;
        this.idPasswordConfig = idPasswordConfig;
        this.logoutHandler = logoutHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .logout()
                    .logoutUrl(PathConstant.Auth.AUTH_PREFIX + PathConstant.Auth.PATH_LOGOUT)
                    .logoutSuccessHandler(logoutHandler)
                    .permitAll()
                .and()
                .authorizeRequests()
                    .antMatchers(getAnonymousUrls()).permitAll()
                    .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf().disable()
                .apply(securityCodeConfig)
                .and()
                .apply(idPasswordConfig)
                .and()
                .apply(emailSecurityConfig);
    }


    /**
     * 获取标有注解 AnonymousAccess 的访问路径
     */
    private String[] getAnonymousUrls() {
        // 获取所有的 RequestMapping
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> allAnonymousAccess = new HashSet<>();
        // 循环 RequestMapping
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethods.entrySet()) {
            HandlerMethod value = infoEntry.getValue();
            // 获取方法上 AnonymousAccess 类型的注解
            PermitAll methodAnnotation = value.getMethodAnnotation(PermitAll.class);
            // 如果方法上标注了 AnonymousAccess 注解，就获取该方法的访问全路径
            if (methodAnnotation != null) {
                allAnonymousAccess.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }

        // 不能用注解的路径
        allAnonymousAccess.add(PathConstant.Auth.AUTH_PREFIX + PathConstant.Auth.PATH_EMAIL_LOGIN);
        allAnonymousAccess.add(PathConstant.Auth.AUTH_PREFIX + PathConstant.Auth.PATH_LOGOUT);

        return allAnonymousAccess.toArray(new String[0]);
    }
}
