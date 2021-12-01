package top.gytf.family.server.security.code.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import top.gytf.family.server.exceptions.SecurityCodeSendException;
import top.gytf.family.server.security.code.SecurityCodeSender;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 邮箱验证码发送器<br>
 * CreateDate:  2021/11/27 22:54 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
@Slf4j
public class EmailSecurityCodeSender implements SecurityCodeSender<EmailSecurityCode> {
    private final static String TAG = EmailSecurityCodeSender.class.getName();

    private final JavaMailSender sender;

    public EmailSecurityCodeSender(JavaMailSender sender) {
        this.sender = sender;
    }

    /**
     * 发送验证码
     *
     * @param code 验证码
     * @throws SecurityCodeSendException 发送错误
     */
    @Override
    public void send(EmailSecurityCode code) throws SecurityCodeSendException {
        log.debug(code.toString());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("2632699773@qq.com");
        mailMessage.setTo(code.getDesc());
        mailMessage.setSubject("【Family】邮箱验证");
        mailMessage.setText("您正在使用DisStudio服务。\n【Family】的验证码为：" + code.getCode() + "，若非本人操作请忽略。");
        try {
            sender.send(mailMessage);
        } catch (MailException e) {
            throw new SecurityCodeSendException(e.getMessage());
        }
    }
}
