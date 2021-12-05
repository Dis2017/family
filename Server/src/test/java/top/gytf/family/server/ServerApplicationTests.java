package top.gytf.family.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import top.gytf.family.server.security.code.SecurityCodeVerifyFilter;

import java.util.Map;

@SpringBootTest
@Slf4j
class ServerApplicationTests {

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        Map<String, SecurityCodeVerifyFilter> map = context.getBeansOfType(SecurityCodeVerifyFilter.class);
        log.info(map.toString());
    }

}
