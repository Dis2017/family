package top.gytf.family.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.gytf.family.server.config.security.SecurityConfig;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.mapper.UserMapper;

import java.util.List;

@SpringBootTest
@Slf4j
class ServerApplicationTests {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void contextLoads() {
        log.debug("point");
    }

}
