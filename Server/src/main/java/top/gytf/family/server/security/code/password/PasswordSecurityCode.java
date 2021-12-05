package top.gytf.family.server.security.code.password;

import top.gytf.family.server.security.code.AbstractSecurityCode;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 密码验证码<br>
 * CreateDate:  2021/12/5 18:56 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class PasswordSecurityCode extends AbstractSecurityCode<Object> {
    private final static String TAG = PasswordSecurityCode.class.getName();

    /**
     * 构造器
     *
     * @param survivalTime 存活时长
     * @param code         验证码
     */
    public PasswordSecurityCode(long survivalTime, String code) {
        super(survivalTime, code);
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public Object getDesc() {
        return null;
    }
}
