package top.gytf.family.server.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import top.gytf.family.server.constants.PathConstant;

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

    public SecurityConfig(EmailSecurityConfig emailSecurityConfig) {
        this.emailSecurityConfig = emailSecurityConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .and()
                .logout()
                .and()
                .authorizeRequests()
                    .antMatchers(PathConstant.PATH_ALL_AUTH).permitAll()
                    .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .apply(emailSecurityConfig);
    }
}
