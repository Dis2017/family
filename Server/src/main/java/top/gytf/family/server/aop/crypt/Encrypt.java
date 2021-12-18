package top.gytf.family.server.aop.crypt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project:     IntelliJ IDEA<br> Description: 加密注解<br> CreateDate:  2021/12/19 0:39 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypt {
	/**
	 * 需要加密的参数索引值<br>
	 * 对同一个参数同时开启加密解密不生效<br>
	 * @return 参数索引
	 */
	int[] args() default {};

	/**
	 * 是否开启后置加密<br>
	 * 对同一个参数同时开启加密解密不生效<br>
	 * @return 是否启用后置加密
	 */
	boolean post() default false;
}
