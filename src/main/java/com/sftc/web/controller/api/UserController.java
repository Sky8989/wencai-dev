package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("user")
@Controller
public class UserController extends AbstractBasicController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse login(@RequestBody UserParam userParam) throws Exception {
        return userService.login(userParam);
    }

    @RequestMapping(value = "/superLogin", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse superLogin(@RequestBody UserParam userParam) throws Exception {
        return userService.superLogin(userParam);
    }

    @RequestMapping(value = "/unbind/common", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse CommonUnbind(@RequestBody  Object object) throws Exception {
        return userService.deleteMobile(object);
    }
}
