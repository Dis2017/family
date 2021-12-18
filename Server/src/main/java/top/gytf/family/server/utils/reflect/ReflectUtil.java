package top.gytf.family.server.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Project:     IntelliJ IDEA<br> Description: 反射工具<br> CreateDate:  2021/12/19 1:14 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class ReflectUtil {

	private static final Map<MethodInfo, Method> METHOD_CACHE = new HashMap<>();

	/**
	 * 获取方法（缓存）
	 * @param methodInfo 方法信息
	 * @return 方法
	 * @throws NoSuchMethodException 找不到方法
	 */
	public static Method getMethod(MethodInfo methodInfo) throws NoSuchMethodException {
		// 查询
		Method method = METHOD_CACHE.get(methodInfo);
		// 不在缓存
		if (method == null) {
			synchronized (METHOD_CACHE) {
				method = METHOD_CACHE.get(methodInfo);
				// 等待的时间没有写入
				if (method == null) {
					// 取
					method = methodInfo.getClazz().getDeclaredMethod(
							methodInfo.getName(),
							methodInfo.getParameterTypes()
					);
					method.setAccessible(true);
					// 缓存
					METHOD_CACHE.put(methodInfo, method);
				}
			}
		}
		return method;
	}

	/**
	 * 获取注解
	 * @param clazz 注解类型
	 * @param methodInfo 方法信息
	 * @param <T> 注解泛型
	 * @return 注解实体
	 */
	public static  <T extends Annotation> T getAnnotation(Class<T> clazz, MethodInfo methodInfo)
			throws NoSuchMethodException {
		return getMethod(methodInfo).getAnnotation(clazz);
	}

}
