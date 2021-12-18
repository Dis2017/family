package top.gytf.family.server.security.code.password;

import org.springframework.stereotype.Component;
import top.gytf.family.server.aop.crypt.Decrypt;

import javax.servlet.http.HttpServletRequest;

/**
 * Project:     IntelliJ IDEA<br> Description: 密码验证码请求验证器参数获取器<br> CreateDate:  2021/12/19 0:21 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class PasswordSecurityCodeRequestValidatorParamGetter {
	private static final String SECURITY_CODE_KEY = "password_code";

	/**
	 * 验证码<br>
	 * 返回值使用aop进行后置解密<br>
	 *
	 * @param request 请求
	 * @return 验证码
	 */
	@Decrypt(post = true)
	public String getCode(HttpServletRequest request) {
		String code = null;
		Object obj = request.getAttribute(SECURITY_CODE_KEY);
		if (obj instanceof String) {
			code = (String) obj;
		}
		if (code == null) {
			code = request.getParameter(SECURITY_CODE_KEY);
		}
		return code;
	}
}
