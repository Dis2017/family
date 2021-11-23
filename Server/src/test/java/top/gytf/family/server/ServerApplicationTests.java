package top.gytf.family.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.mapper.UserMapper;

import java.util.List;

@SpringBootTest
@Slf4j
class ServerApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        User userDao = User.builder()
                .name("Test")
                .password("cnmd")
                .build();
        userMapper.insert(userDao);

        List<User> users = userMapper.selectList(null);
        for (User u : users) log.debug(u.toString());
    }

}
