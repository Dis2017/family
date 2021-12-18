package top.gytf.family.server.utils.reflect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Project:     IntelliJ IDEA<br> Description: 方法信息<br> CreateDate:  2021/12/19 1:16 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Data
@Builder
@AllArgsConstructor
public class MethodInfo {
	/**
	 * 所属类
	 */
	private Class clazz;
	/**
	 * 方法名
	 */
	private String name;
	/**
	 * 方法参数类型
	 */
	private Class[] parameterTypes;

	/**
	 * 通过方法签名创建实例
	 * @param signature 方法签名
	 */
	public MethodInfo(MethodSignature signature) {
		this.clazz = signature.getDeclaringType();
		this.name = signature.getName();
		this.parameterTypes = signature.getParameterTypes();
	}
}
