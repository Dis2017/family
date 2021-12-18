package top.gytf.family.server.aop.crypt;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.gytf.family.server.utils.RsaUtil;
import top.gytf.family.server.utils.reflect.MethodInfo;
import top.gytf.family.server.utils.reflect.ReflectUtil;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 加解密器<br>
 * CreateDate:  2021/12/18 22:24 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
@Aspect
@Slf4j
public class CryptHandler {
	private final Map<Class, Decrypt> DECRYPT_CACHE;
	private final Map<Class, Encrypt> ENCRYPT_CACHE;
	private final RsaUtil rsaUtil;

	public CryptHandler() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
		DECRYPT_CACHE = new ConcurrentHashMap<>();
		ENCRYPT_CACHE = new ConcurrentHashMap<>();
		rsaUtil = RsaUtil.getInstance();
	}

	/**
	 * 获取这个签名上的Decrypt注解（缓存）
	 * @param joinPoint 切入点
	 * @return 注解
	 */
	private synchronized Decrypt getDecrypt(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Class clazz = signature.getDeclaringType();
		Decrypt decrypt = DECRYPT_CACHE.get(clazz);

		if (decrypt == null) {
			try {
				decrypt = ReflectUtil.getAnnotation(Decrypt.class, new MethodInfo(signature));
			} catch (NoSuchMethodException ignored) {
			}
			if (decrypt != null) {
				DECRYPT_CACHE.put(clazz, decrypt);
			}
		}

		return decrypt;
	}

	/**
	 * 获取这个签名上的Encrypt注解（缓存）
	 * @param joinPoint 切入点
	 * @return 注解
	 */
	private synchronized Encrypt getEncrypt(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Class clazz = signature.getDeclaringType();
		Encrypt encrypt = ENCRYPT_CACHE.get(clazz);

		if (encrypt == null) {
			try {
				encrypt = ReflectUtil.getAnnotation(Encrypt.class, new MethodInfo(signature));
			} catch (NoSuchMethodException ignored) {
			}
			if (encrypt != null) {
				ENCRYPT_CACHE.put(clazz, encrypt);
			}
		}

		return encrypt;
	}

	/**
	 * 所有标注了{@link top.gytf.family.server.aop.crypt.Decrypt}注解的方法
	 */
	@Pointcut("@annotation(Decrypt)")
	public void allDecryptApi() {}

	/**
	 * 所有标注了{@link top.gytf.family.server.aop.crypt.Encrypt}注解的方法
	 */
	@Pointcut("@annotation(Encrypt)")
	public void allEncryptApi() {}

	/**
	 * 处理加解密
	 * @param joinPoint 切入点
	 * @return 返回值
	 * @throws Throwable 错误
	 */
	@Around("allDecryptApi() || allEncryptApi()")
	public Object processCrypt(ProceedingJoinPoint joinPoint) throws Throwable {
		// 取出注解
		Decrypt decrypt = getDecrypt(joinPoint);
		Encrypt encrypt = getEncrypt(joinPoint);

		// 处理的参数
		Object[] args = joinPoint.getArgs();
		// 加解密的参数
		Set<Integer> decryptIndexes = decrypt == null ?
				Collections.emptySet() :
				Arrays.stream(decrypt.args())
						.boxed()
						.collect(Collectors.toSet());
		Set<Integer> encryptIndexes = encrypt == null ?
				Collections.emptySet() :
				Arrays.stream(encrypt.args())
						.boxed()
						.collect(Collectors.toSet());

		// 前置解密
		for (Integer decryptIndex : decryptIndexes) {
			if (encryptIndexes.contains(decryptIndex)) {
				encryptIndexes.remove(decryptIndex);
				continue;
			}
			Object arg = args[decryptIndex];
			if (arg instanceof String) {
				args[decryptIndex] = rsaUtil.decrypted((String) arg);
			}
		}
		// 前置加密
		for (Integer encryptIndex : encryptIndexes) {
			Object arg = args[encryptIndex];
			if (arg instanceof String) {
				args[encryptIndex] = rsaUtil.encrypted((String) arg);
			}
		}

		// 处理
		Object result = joinPoint.proceed(args);

		if (result instanceof String) {
			boolean postDecrypt = decrypt != null && decrypt.post();
			boolean postEncrypt = encrypt != null && encrypt.post();

			if (postDecrypt && !postEncrypt) {
				// 后置解密
				result = rsaUtil.decrypted((String) result);
			} else if (!postDecrypt && postEncrypt) {
				// 后置加密
				result = rsaUtil.encrypted((String) result);
			}
		}

		return result;
	}
}
