package top.gytf.family.server.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.security.login.LoginHandler;
import top.gytf.family.server.security.code.SecurityCodeVerifyStrategy;
import top.gytf.family.server.security.code.email.EmailSecurityCodeRequestValidator;
import top.gytf.family.server.security.login.email.EmailAuthenticationFilter;
import top.gytf.family.server.security.login.email.EmailAuthenticationProvider;

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
@SecurityCodeVerifyStrategy(  //邮箱验证码验证
        value = {EmailSecurityCodeRequestValidator.class},
        patterns = {PathConstant.Auth.AUTH_PREFIX + PathConstant.Auth.PATH_EMAIL_LOGIN}
)
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
                .addFilterAfter(getEmailAuthenticationFilter(builder), FilterSecurityInterceptor.class);
    }

    public EmailAuthenticationFilter getEmailAuthenticationFilter(HttpSecurity httpSecurity) {
        EmailAuthenticationFilter emailAuthenticationFilter = new EmailAuthenticationFilter();
        emailAuthenticationFilter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));
        emailAuthenticationFilter.setAuthenticationSuccessHandler(loginHandler);
        emailAuthenticationFilter.setAuthenticationFailureHandler(loginHandler);
        return emailAuthenticationFilter;
    }
}
