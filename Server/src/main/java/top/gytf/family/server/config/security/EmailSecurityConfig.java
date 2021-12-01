package top.gytf.family.server.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import top.gytf.family.server.security.LoginHandler;
import top.gytf.family.server.security.email.EmailAuthenticationFilter;
import top.gytf.family.server.security.email.EmailAuthenticationProvider;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   EmailSecurityConfig
 * Description: 电子邮箱安全配置
 * CreateDate:  2021/11/25 15:53
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class EmailSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final static String TAG = EmailSecurityConfig.class.getName();

    private final EmailAuthenticationProvider emailAuthenticationProvider;
    private final LoginHandler loginHandler;

    public EmailSecurityConfig(EmailAuthenticationProvider emailAuthenticationProvider, LoginHandler loginHandler) {
        this.emailAuthenticationProvider = emailAuthenticationProvider;
        this.loginHandler = loginHandler;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        super.configure(builder);

        builder
                .authenticationProvider(emailAuthenticationProvider)
                .addFilterAfter(getEmailAuthenticationFilter(builder), UsernamePasswordAuthenticationFilter.class);
    }

    public EmailAuthenticationFilter getEmailAuthenticationFilter(HttpSecurity httpSecurity) {
        EmailAuthenticationFilter emailAuthenticationFilter = new EmailAuthenticationFilter();
        emailAuthenticationFilter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));
        emailAuthenticationFilter.setAuthenticationSuccessHandler(loginHandler);
        emailAuthenticationFilter.setAuthenticationFailureHandler(loginHandler);
        return emailAuthenticationFilter;
    }
}
