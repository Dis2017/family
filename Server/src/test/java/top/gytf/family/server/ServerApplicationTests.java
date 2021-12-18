package top.gytf.family.server;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import top.gytf.family.server.entity.User;

@SpringBootTest
@Slf4j
class ServerApplicationTests {

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        String text = "";
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("name").eq("sex", "true").or().eq("email", "null");
        log.debug(wrapper.getSqlSegment());
    }

}
