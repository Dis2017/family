package top.gytf.family.server.security.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import top.gytf.family.server.constants.SessionConstant;
import top.gytf.family.server.security.*;

import javax.servlet.http.HttpSession;
import java.util.Random;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   EmailSecurityCodeHandler
 * Description: 邮箱验证码处理器
 * CreateDate:  2021/11/26 22:52
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class EmailSecurityCodeHandler implements SecurityCodeHandler<NumberSecurityCode> {
    private final static String TAG = EmailSecurityCodeHandler.class.getName();

    /**
     * 验证码位数
     */
    @Setter
    @Getter
    private int size = 5;

    /**
     * 验证码存活时间
     */
    @Setter
    @Getter
    private int survivalTime = 300;

    private final Random random;

    public EmailSecurityCodeHandler() {
        this.random = new Random(System.currentTimeMillis());
    }

    /**
     * 生成验证码
     *
     * @param session 会话
     * @return 验证码
     */
    @Override
    public NumberSecurityCode generate(HttpSession session) {
        NumberSecurityCode code = null;
        Object codeObj =  session.getAttribute(SessionConstant.KEY_EMAIL_SECURITY_CODE);
        if (codeObj instanceof NumberSecurityCode) code = (NumberSecurityCode) codeObj;

        if (code == null || code.isExpired()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                builder.append(random.nextInt(10));
            }
            code = new NumberSecurityCode(builder.toString(), survivalTime);
            session.setAttribute(SessionConstant.KEY_EMAIL_SECURITY_CODE, code);
        }

        return code;
    }

    /**
     * 验证
     *
     * @param session 会话
     * @param stringCode    待验证码
     * @throws SecurityCodeException 验证码错误
     */
    @Override
    public void verify(HttpSession session, String stringCode) throws SecurityCodeException {
        assert stringCode != null;

        NumberSecurityCode code = null;
        Object codeObj =  session.getAttribute(SessionConstant.KEY_EMAIL_SECURITY_CODE);
        if (codeObj instanceof NumberSecurityCode) code = (NumberSecurityCode) codeObj;

        if (code == null)
            throw new NullSecurityCodeException(session.getId() + "的邮箱验证码不存在");
        if (code.isExpired())
            throw new SecurityCodeExpiredException(code + "验证码过期");
        if (code.getCode() == null || !code.getCode().equals(stringCode))
            throw new SecurityCodeNotMatchException(code + "和" + stringCode + "不匹配");

        destroy(session);
    }

    /**
     * 销毁验证码
     *
     * @param session 会话
     */
    @Override
    public void destroy(HttpSession session) {
        session.removeAttribute(SessionConstant.KEY_EMAIL_SECURITY_CODE);
    }
}


