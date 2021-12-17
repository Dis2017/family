package top.gytf.family.server.security.code.image;

import org.springframework.stereotype.Component;
import top.gytf.family.server.security.code.AbstractSecurityCodeHandler;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 图片验证码处理器<br>
 * CreateDate:  2021/11/28 19:05 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class ImageSecurityCodeHandler extends AbstractSecurityCodeHandler<ServletResponse, ImageSecurityCode, HttpSession> {
    private final static String TAG = ImageSecurityCodeHandler.class.getName();

    /**
     * 构造器
     */
    public ImageSecurityCodeHandler() {
        super(
                new ImageSecurityCodeGenerator(),
                new ImageSecurityCodeSender(),
                new ImageSecurityCodeStorage()
        );
    }
}
