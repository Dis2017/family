package top.gytf.family.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.gytf.family.server.dao.UserDao;
import top.gytf.family.server.mapper.UserMapper;

@SpringBootTest
class ServerApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        UserDao userDao = UserDao.builder()
                .name("Test")
                .build();
        userMapper.insert(userDao);
    }

}
