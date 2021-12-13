package top.gytf.family.server;

import lombok.extern.slf4j.Slf4j;
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
    public void test() throws IOException {
        File file = new File(FileConstant.APPLICATION_ROOT);
        FileReader reader = new FileReader(file);
        reader.close();
    }
}
