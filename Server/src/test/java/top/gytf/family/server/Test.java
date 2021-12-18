package top.gytf.family.server;

import lombok.extern.slf4j.Slf4j;
import top.gytf.family.server.utils.RsaUtil;

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
    public void test() throws Exception {
        RsaUtil rsaUtil = RsaUtil.getInstance();
        log.debug(rsaUtil.getPublicKey());

        String raw = "10010422Ytf";
        String encrypted = rsaUtil.encrypted(raw);
        log.debug(encrypted);
        log.debug(rsaUtil.decrypted(encrypted));
    }
}
