package top.gytf.family.server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 测试<br>
 * CreateDate:  2021/11/29 0:49 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private final static String TAG = TestController.class.getName();

    @GetMapping("hello")
    public String hello() {
        return "Hello World.";
    }
}
