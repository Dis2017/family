package top.gytf.family.server.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.SessionManagementFilter;
import top.gytf.family.server.response.GlobalExceptionHandler;
import top.gytf.family.server.security.access.AccessDecisionFilter;
import top.gytf.family.server.security.code.SecurityCodeVerifyFilter;

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
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final static String TAG = SecurityConfig.class.getName();

    private final GlobalExceptionHandler globalExceptionHandler;
    private final AccessDecisionFilter accessDecisionFilter;
    private final SecurityCodeVerifyFilter securityCodeVerifyFilter;
    private final PasswordAuthenticationConfig passwordAuthenticationConfig;
    private final EmailAuthenticationConfig emailAuthenticationConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 会话管理
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry());

        // 禁用csrf
        http
                .csrf().disable();

        // 禁用logout
        http.
                logout().disable();


        //未登录用户默认授权
        http
                .anonymous()
                    .authorities("ROLE_GUEST");

        //异常处理
        http
                .exceptionHandling().disable()
                .addFilterAfter(globalExceptionHandler, SessionManagementFilter.class);

        //应用其他配置
        http
                //访问控制
                .addFilterAfter(accessDecisionFilter, GlobalExceptionHandler.class)
                //验证码校验
                .addFilterAfter(securityCodeVerifyFilter, AccessDecisionFilter.class)
                //验证
                .apply(passwordAuthenticationConfig).and()
                .apply(emailAuthenticationConfig);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
