package com.sftc.web.controller.api;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.User;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description: 基本用户操作的控制器
 * @date 2017/4/7
 * @Time 上午8:50
 */
@RequestMapping("user")
@Controller
public class UserController extends AbstractBasicController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse login(@RequestBody UserParam userParam) throws Exception {
        return userService.login(userParam);
    }
}
