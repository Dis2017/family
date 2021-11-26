package top.gytf.family.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.session.StandardSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.mock.web.MockHttpSession;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.mapper.UserMapper;
import top.gytf.family.server.security.NumberSecurityCode;
import top.gytf.family.server.security.email.EmailSecurityCodeHandler;

import javax.servlet.http.HttpSession;

@SpringBootTest
@Slf4j
class ServerApplicationTests {

    @Autowired
    private EmailSecurityCodeHandler emailSecurityCodeHandler;

    @Test
    void contextLoads() {
        HttpSession session = new MockHttpSession();
        NumberSecurityCode code = emailSecurityCodeHandler.generate(session);
        log.debug(code.toString());
    }

}
