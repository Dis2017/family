package top.gytf.family.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.gytf.family.server.security.email.EmailSecurityCodeHandler;

@SpringBootTest
@Slf4j
class ServerApplicationTests {

    @Autowired
    private EmailSecurityCodeHandler emailSecurityCodeHandler;

    @Test
    void contextLoads() {
    }

}
