package top.gytf.family.server.security.image;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import top.gytf.family.server.exceptions.SecurityCodeGenerateException;
import top.gytf.family.server.security.SecurityCodeGenerator;
import top.gytf.family.server.security.email.EmailSecurityCode;

import javax.servlet.ServletResponse;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 图片验证码生成器<br>
 * CreateDate:  2021/11/28 17:50 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class ImageSecurityCodeGenerator implements SecurityCodeGenerator<ServletResponse, ImageSecurityCode> {
    private final static String TAG = ImageSecurityCodeGenerator.class.getName();

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

    public ImageSecurityCodeGenerator() {
        this.random = new Random(System.currentTimeMillis());
    }

    /**
     * 生成验证码
     *
     * @param desc 描述
     * @return 验证码
     * @throws SecurityCodeGenerateException 验证码生成错误
     */
    @Override
    public ImageSecurityCode generate(ServletResponse desc) throws SecurityCodeGenerateException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(random.nextInt(10));
        }
        return new ImageSecurityCode(LocalDateTime.now().plusSeconds(survivalTime), builder.toString(), desc);
    }
}
