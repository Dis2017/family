package top.gytf.family.server.security.login.password;

import org.springframework.stereotype.Component;
import top.gytf.family.server.aop.crypt.Decrypt;
import top.gytf.family.server.exceptions.EmptyParamException;

import javax.servlet.http.HttpServletRequest;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 密码验证码参数获取器<br>
 * CreateDate:  2021/12/18 23:25 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class PasswordAuthenticationParamGetter {
	/**
	 * 从请求中获取密码<br>
	 * 使用AOP进行后置解密<br>
	 *
	 * @param request 请求
	 * @return 密码
	 */
	@Decrypt(post = true)
	public String getPassword(HttpServletRequest request) {
		final String password = request.getParameter("password");
		if (password == null) {
			throw new EmptyParamException("password参数缺失。");
		}
		return password;
	}

	/**
	 * 从请求中获取凭证
	 * @param request 请求
	 * @return id
	 */
	public String getCredential(HttpServletRequest request) {
		final String credential = request.getParameter("credential");
		if (credential == null) {
			throw new EmptyParamException("credential参数缺失。");
		}
		return credential;
	}
}
