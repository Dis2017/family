package top.gytf.family.server.security.code;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码验证策略<br>
 * CreateDate:  2021/12/5 16:25 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityCodeVerifyStrategy {
    /**
     * 只需要验证一项<br>
     * 默认全部通过才算验证完成
     */
    boolean only() default false;

    /**
     * 需求的验证器
     * @return 验证器
     */
    Class<? extends SecurityCodeRequestValidator<?, ?>>[] value();

    /**
     * 仅当注解在类上时才使用，指定绑定的url
     */
    String[] patterns() default {};

    /**
     * 仅当注解在类上时才使用，指定绑定的请求方式
     */
    RequestMethod[] methods() default {};
}
