package top.gytf.family.server.config.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.response.AccessDeniedHandlerImpl;
import top.gytf.family.server.response.AuthenticationEntryPointImpl;
import top.gytf.family.server.security.LogoutHandler;
import top.gytf.family.server.security.auth.AccessDecisionManagerImpl;
import top.gytf.family.server.security.auth.FilterInvocationSecurityMetadataSourceImpl;

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
    private final AccessDecisionManagerImpl accessDecisionManager;
    private final FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    public SecurityConfig(ApplicationContext applicationContext, SecurityCodeConfig securityCodeConfig, EmailSecurityConfig emailSecurityConfig, IdPasswordConfig idPasswordConfig, LogoutHandler logoutHandler, AccessDeniedHandlerImpl accessDeniedHandler, AuthenticationEntryPointImpl authenticationEntryPoint, AccessDecisionManagerImpl accessDecisionManager, FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource) {
        this.applicationContext = applicationContext;
        this.securityCodeConfig = securityCodeConfig;
        this.emailSecurityConfig = emailSecurityConfig;
        this.idPasswordConfig = idPasswordConfig;
        this.logoutHandler = logoutHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDecisionManager = accessDecisionManager;
        this.filterInvocationSecurityMetadataSource = filterInvocationSecurityMetadataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //会话管理
        http
                .sessionManagement()
                    .maximumSessions(1)
                    .sessionRegistry(sessionRegistry());

        //登出
        http
                .logout()
                    .logoutUrl(PathConstant.Auth.AUTH_PREFIX + PathConstant.Auth.PATH_LOGOUT)
                    .logoutSuccessHandler(logoutHandler)
                    .permitAll();

        //未登录用户默认授权
        http
                .anonymous()
                    .authorities("ROLE_GUEST");

        //访问控制
        http
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                        @Override
                        public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                            fsi.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
                            fsi.setAccessDecisionManager(accessDecisionManager);
                            return fsi;
                        }
                    });

        //异常处理
        http
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(authenticationEntryPoint);

        //禁用csrf
        http
                .csrf().disable();

        //应用其他配置
        http
                .apply(securityCodeConfig).and()
                .apply(idPasswordConfig).and()
                .apply(emailSecurityConfig);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
