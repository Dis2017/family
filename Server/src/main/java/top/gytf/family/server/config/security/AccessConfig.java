package top.gytf.family.server.config.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;
import top.gytf.family.server.security.access.AccessDecisionFilter;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 访问配置<br>
 * CreateDate:  2021/12/12 22:07 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class AccessConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final static String TAG = AccessConfig.class.getName();

    private final AccessDecisionFilter accessDecisionFilter;

    public AccessConfig(AccessDecisionFilter accessDecisionFilter) {
        this.accessDecisionFilter = accessDecisionFilter;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        super.configure(builder);
        builder
                .addFilterAfter(accessDecisionFilter, FilterSecurityInterceptor.class);
    }
}
