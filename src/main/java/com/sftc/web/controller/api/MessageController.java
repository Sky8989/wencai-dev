package com.sftc.web.controller.api;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("sf")
public class MessageController extends AbstractBasicController {

    /**
     * 获取短信接口
     */
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseBody
    APIResponse message(@RequestBody Object obj) throws Exception {
        return messageService.getMessage(obj);
    }

    /**
     * 注册接口
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    APIResponse register(@RequestBody Object obj) throws Exception {
        return messageService.register(obj);
    }

    /**
     * 获取TOKEN接口
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    APIResponse getToken(@RequestBody Object obj) throws Exception {
        return messageService.getToken(obj);
    }

    /**
     * 登录接口
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    APIResponse login(@RequestBody Object obj) throws Exception {
        return messageService.login(obj);
    }

//    /**
//     * 获取个人信息
//     */
//    @RequestMapping(value = "/loginByGet", method = RequestMethod.GET)
//    @ResponseBody
//    APIResponse loginByGet(String access_token) throws Exception {
//        return messageService.loginByGet(access_token);
//    }

    /**
     * 获取个人信息
     */
    @RequestMapping(value = "/loginByGet", method = RequestMethod.POST)
    @ResponseBody
    APIResponse loginByGet(@RequestBody Map paramMap) throws Exception {
        return messageService.loginByGet(paramMap);
    }

    @RequestMapping(value = "/quotes", method = RequestMethod.POST)
    void quotes(@RequestBody Result r) throws Exception {
        System.out.println(r.getRequest().getPackages().get(0).getType() + r.getRequest().getPackages().get(1).getType());
    }

}
