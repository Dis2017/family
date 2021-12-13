package top.gytf.family.server.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import top.gytf.family.server.response.GlobalExceptionHandler;

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

    private final SecurityCodeConfig securityCodeConfig;
    private final EmailSecurityConfig emailSecurityConfig;
    private final IdPasswordConfig idPasswordConfig;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final AccessConfig accessConfig;

    public SecurityConfig(SecurityCodeConfig securityCodeConfig, EmailSecurityConfig emailSecurityConfig, IdPasswordConfig idPasswordConfig, GlobalExceptionHandler globalExceptionHandler, AccessConfig accessConfig) {
        this.securityCodeConfig = securityCodeConfig;
        this.emailSecurityConfig = emailSecurityConfig;
        this.idPasswordConfig = idPasswordConfig;
        this.globalExceptionHandler = globalExceptionHandler;
        this.accessConfig = accessConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //会话管理
        http
                .sessionManagement()
                    .maximumSessions(1)
                    .sessionRegistry(sessionRegistry());

        //所有都需要权限
        http
                .authorizeRequests()
                    .anyRequest().permitAll();

        //登出
        http
                .logout()
                    .disable();

        //未登录用户默认授权
        http
                .anonymous()
                    .authorities("ROLE_GUEST");

        //异常处理
        http
                .exceptionHandling()
                    .accessDeniedHandler(globalExceptionHandler)
                    .authenticationEntryPoint(globalExceptionHandler);

        //禁用csrf
        http
                .csrf().disable();

        //应用其他配置
        /*
        AccessDecisionFilter
        SecurityCodeVerifyFilter
        IdPasswordAuthenticationFilter
        EmailAuthenticationFilter
         */
        http
                .apply(accessConfig).and()
                .apply(securityCodeConfig).and()
                .apply(idPasswordConfig).and()
                .apply(emailSecurityConfig);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
