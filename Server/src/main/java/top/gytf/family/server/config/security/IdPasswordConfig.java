package top.gytf.family.server.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;
import top.gytf.family.server.security.login.LoginHandler;
import top.gytf.family.server.security.login.id.IdPasswordAuthenticationFilter;
import top.gytf.family.server.security.login.id.IdPasswordAuthenticationProvider;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 编号密码配置<br>
 * CreateDate:  2021/12/1 19:24 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class IdPasswordConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final static String TAG = IdPasswordConfig.class.getName();

    private final IdPasswordAuthenticationProvider idPasswordAuthenticationProvider;
    private final LoginHandler loginHandler;

    public IdPasswordConfig(IdPasswordAuthenticationProvider idPasswordAuthenticationProvider, LoginHandler loginHandler) {
        this.idPasswordAuthenticationProvider = idPasswordAuthenticationProvider;
        this.loginHandler = loginHandler;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        super.configure(builder);
        builder
                .authenticationProvider(idPasswordAuthenticationProvider)
                .addFilterAfter(getIdPasswordAuthenticationFilter(builder), FilterSecurityInterceptor.class);
    }

    public IdPasswordAuthenticationFilter getIdPasswordAuthenticationFilter(HttpSecurity httpSecurity) {
        IdPasswordAuthenticationFilter idPasswordAuthenticationFilter = new IdPasswordAuthenticationFilter();
        idPasswordAuthenticationFilter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));
        idPasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginHandler);
        idPasswordAuthenticationFilter.setAuthenticationFailureHandler(loginHandler);
        return idPasswordAuthenticationFilter;
    }
}
