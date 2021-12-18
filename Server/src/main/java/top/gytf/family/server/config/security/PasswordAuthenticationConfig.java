package top.gytf.family.server.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;
import top.gytf.family.server.security.login.LoginHandler;
import top.gytf.family.server.security.login.password.PasswordAuthenticationFilter;
import top.gytf.family.server.security.login.password.PasswordAuthenticationParamGetter;
import top.gytf.family.server.security.login.password.PasswordAuthenticationProvider;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 密码配置<br>
 * CreateDate:  2021/12/1 19:24 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class PasswordAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final static String TAG = PasswordAuthenticationConfig.class.getName();

    private final PasswordAuthenticationProvider passwordAuthenticationProvider;
    private final LoginHandler loginHandler;
    private final PasswordAuthenticationParamGetter getter;

    public PasswordAuthenticationConfig(PasswordAuthenticationProvider passwordAuthenticationProvider, LoginHandler loginHandler, PasswordAuthenticationParamGetter getter) {
        this.passwordAuthenticationProvider = passwordAuthenticationProvider;
        this.loginHandler = loginHandler;
        this.getter = getter;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        super.configure(builder);
        builder
                .authenticationProvider(passwordAuthenticationProvider)
                .addFilterAfter(getIdPasswordAuthenticationFilter(builder), FilterSecurityInterceptor.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public PasswordAuthenticationFilter getIdPasswordAuthenticationFilter(HttpSecurity security)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        PasswordAuthenticationFilter passwordAuthenticationFilter = new PasswordAuthenticationFilter(getter);
        passwordAuthenticationFilter.setAuthenticationManager(security.getSharedObject(AuthenticationManager.class));
        passwordAuthenticationFilter.setAuthenticationSuccessHandler(loginHandler);
        passwordAuthenticationFilter.setAuthenticationFailureHandler(loginHandler);
        return passwordAuthenticationFilter;
    }
}
