package top.gytf.family.server.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.security.image.ImageSecurityCodeVerifyFilter;

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

    private final EmailSecurityConfig emailSecurityConfig;
    private final ImageSecurityCodeVerifyFilter filter;

    public SecurityConfig(EmailSecurityConfig emailSecurityConfig, ImageSecurityCodeVerifyFilter filter) {
        this.emailSecurityConfig = emailSecurityConfig;
        this.filter = filter;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .and()
                .logout()
                .and()
                .authorizeRequests()
                    .antMatchers(PathConstant.Auth.PATH_ALL_AUTH).permitAll()
                    .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .apply(emailSecurityConfig);
    }
}
