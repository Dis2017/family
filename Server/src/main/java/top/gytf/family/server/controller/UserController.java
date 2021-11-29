package top.gytf.family.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.entity.User;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户控制器<br>
 * CreateDate:  2021/11/29 23:01 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@RestController
@RequestMapping(PathConstant.User.USER_PREFIX)
public class UserController {
    private final static String TAG = UserController.class.getName();

}
