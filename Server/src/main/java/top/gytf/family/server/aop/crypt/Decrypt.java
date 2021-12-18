package top.gytf.family.server.aop.crypt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project:     IntelliJ IDEA<br> Description: 标注为需要解码<br> CreateDate:  2021/12/18 22:30 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Decrypt {
	/**
	 * 需要解密的参数索引值<br>
	 * 对同一个参数同时开启加密解密不生效<br>
	 * @return 参数索引
	 */
	int[] args() default {};

	/**
	 * 是否开启后置解密<br>
	 * 对同一个参数同时开启加密解密不生效<br>
	 * @return 是否启用后置解密
	 */
	boolean post() default false;
}
