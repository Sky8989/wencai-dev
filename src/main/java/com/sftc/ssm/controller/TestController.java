package com.sftc.ssm.controller;

import com.sftc.ssm.model.User;
import com.sftc.ssm.tools.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("test")
    public
    @ResponseBody
    APIResponse test() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("傻逼");
        user.setEmail("222@qq.com");
        return APIUtil.getResponse(APIStatus.SUCCESS.getState(), APIStatus.SUCCESS.getMessage(), user);
    }
}
