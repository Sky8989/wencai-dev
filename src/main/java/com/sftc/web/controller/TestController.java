package com.sftc.web.controller;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.ssm.controller
 * @Description:
 * @date 17/4/1
 * @Time 下午10:45
 */
@Controller
public class TestController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "test")
    @ResponseBody
    APIResponse test() throws Exception {
        return userService.login("23");
    }
}
