package top.gytf.family.server.config.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

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
}
