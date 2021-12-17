package top.gytf.family.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.gytf.family.server.constants.FileConstant;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: <br>
 * CreateDate:  2021/12/12 11:53 <br>
 ------------------------------------------------------------------------------------------
 * @version     V1.0  
 * @author      user
 */
@Slf4j
public class Test {
    private final static String TAG = Test.class.getName();

    @org.junit.jupiter.api.Test
    public void test() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        final String rawPassword = "123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        log.debug(String.valueOf(passwordEncoder.matches(rawPassword, encodedPassword)));
        log.debug(String.valueOf(passwordEncoder.matches(passwordEncoder.encode(rawPassword), encodedPassword)));
    }
}
