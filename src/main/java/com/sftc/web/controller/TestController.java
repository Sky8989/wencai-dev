package com.sftc.web.controller;

import com.sftc.tools.api.APIResponse;
import com.sftc.tools.md5.MD5Util;
import com.sftc.web.model.User;
import com.sftc.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class TestController extends AbstractBasicController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "test")
    @ResponseBody
    APIResponse test() throws Exception {
        return userService.login(new User("skm", MD5Util.MD5("123")));
    }
}
