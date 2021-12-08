package top.gytf.family.server.security.code.email;

import lombok.Getter;
import lombok.Setter;
import top.gytf.family.server.exceptions.SecurityCodeGenerateException;
import top.gytf.family.server.security.code.SecurityCodeGenerator;

import java.util.Random;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 数字验证码生成器<br>
 * CreateDate:  2021/11/27 22:29 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class EmailSecurityCodeGenerator implements SecurityCodeGenerator<String, EmailSecurityCode> {
    private final static String TAG = EmailSecurityCodeGenerator.class.getName();

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

    public EmailSecurityCodeGenerator() {
        this.random = new Random(System.currentTimeMillis());
    }

    /**
     * 生成验证码<br>
     * 在{@link top.gytf.family.server.security.code.SecurityCodeHandler#generate}中调用
     * @param desc 描述
     * @return 验证码
     * @throws SecurityCodeGenerateException 验证码生成错误
     */
    @Override
    public EmailSecurityCode generate(String desc) throws SecurityCodeGenerateException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(random.nextInt(10));
        }
        return new EmailSecurityCode(survivalTime, builder.toString(), desc);
    }
}
