package top.gytf.family.server.config.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;
import top.gytf.family.server.security.code.SecurityCodeVerifyFilter;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码配置<br>
 * CreateDate:  2021/12/5 14:57 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class SecurityCodeConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final static String TAG = SecurityCodeConfig.class.getName();

    private final SecurityCodeVerifyFilter securityCodeVerifyFilter;

    public SecurityCodeConfig(SecurityCodeVerifyFilter securityCodeVerifyFilter) {
        this.securityCodeVerifyFilter = securityCodeVerifyFilter;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        super.configure(builder);

        builder
                .addFilterBefore(securityCodeVerifyFilter, LogoutFilter.class);
    }
}
