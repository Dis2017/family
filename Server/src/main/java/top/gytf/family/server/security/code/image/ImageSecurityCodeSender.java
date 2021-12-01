package top.gytf.family.server.security.code.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import top.gytf.family.server.exceptions.SecurityCodeSendException;
import top.gytf.family.server.security.code.SecurityCodeSender;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 图片验证码发送器<br>
 * CreateDate:  2021/11/28 17:41 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
@Slf4j
public class ImageSecurityCodeSender implements SecurityCodeSender<ImageSecurityCode> {
    private final static String TAG = ImageSecurityCodeSender.class.getName();

    /**
     * 图片验证码宽度
     */
    public static final int IMAGE_WIDTH = 70;
    /**
     * 图片验证码高度
     */
    public static final int IMAGE_HEIGHT = 30;

    private final Random random;

    public ImageSecurityCodeSender() {
        this.random = new Random(System.currentTimeMillis());
    }

    /**
     * 发送验证码
     *
     * @param code 验证码
     * @throws SecurityCodeSendException 发送错误
     */
    @Override
    public void send(ImageSecurityCode code) throws SecurityCodeSendException {
        log.debug(code.toString());
        try {
            code.getDesc().setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(generateImage(code.getCode()), "JPEG", code.getDesc().getOutputStream());
        } catch (IOException e) {
            throw new SecurityCodeSendException(e.getMessage());
        }
    }

    /**
     * 生成验证码图片
     * @param code 验证码文本
     * @return 图片
     */
    private BufferedImage generateImage(String code) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        g.setColor(getRandColor(200, 255));
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(IMAGE_WIDTH);
            int y = random.nextInt(IMAGE_HEIGHT);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.setColor(getRandColor(100, 200));
            g.drawLine(x, y, x + xl, y + yl);
        }

        g.setFont(new Font(null, Font.BOLD + Font.ITALIC, 14));
        int y = (IMAGE_HEIGHT - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
        int textW = (int) (g.getFontMetrics().stringWidth(code) * 1.5);
        int x = (IMAGE_WIDTH - textW) / 2;
        for (int i = 0; i < code.length(); i++) {
            g.setColor(getRandColor(0, 100));
            char ch = code.charAt(i);
            int charW = (int) (g.getFontMetrics().charWidth(ch) * 1.5);
            x += (int) (charW * 0.25);
            g.drawString(String.valueOf(code.charAt(i)), x, y);
            x += (int) (charW * 0.75);
        }

        return image;
    }



    /**
     * 生成随机背景条纹
     *
     * @param begin 开始值
     * @param end 结束值
     * @return 颜色
     */
    private Color getRandColor(int begin, int end) {
        if (begin > 255) {
            begin = 255;
        }
        if (end > 255) {
            end = 255;
        }
        if (begin > end) {
            int t = begin;
            begin = end;
            end = t;
        }

        int bound = end - begin;
        int r = begin + random.nextInt(bound);
        int g = begin + random.nextInt(bound);
        int b = begin + random.nextInt(bound);
        return new Color(r, g, b);
    }
}
