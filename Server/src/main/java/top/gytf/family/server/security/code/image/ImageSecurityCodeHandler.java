package top.gytf.family.server.security.code.image;

import org.springframework.stereotype.Component;
import top.gytf.family.server.constants.SessionConstant;
import top.gytf.family.server.security.code.AbstractSecurityCodeHandler;
import top.gytf.family.server.security.code.AbstractSessionSecurityCodeStorage;

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
                new AbstractSessionSecurityCodeStorage<>() {
                    @Override
                    public boolean isSingle() {
                        return true;
                    }

                    @Override
                    public String getKeyPrefix() {
                        return SessionConstant.KEY_IMAGE_SECURITY_CODE;
                    }

                    @Override
                    public ImageSecurityCode convert(Object obj) {
                        if (!(obj instanceof ImageSecurityCode)) {
                            return null;
                        }
                        return (ImageSecurityCode) obj;
                    }
                }
        );
    }
}
