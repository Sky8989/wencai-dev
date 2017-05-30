package com.sftc.web.controller.api;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _Yangqiyuan
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description: 获取验证码接口
 * @date 2017/5/15
 * @Time 下午14:59
 */
@Controller
@RequestMapping("sf")
public class MessageController extends AbstractBasicController {

    /**
     * 获取短信接口
     */
    @RequestMapping(value = "/message", method = RequestMethod.POST, headers = "api-version=1")
    @ResponseBody
    APIResponse message(@RequestBody Object obj) throws Exception {
       return messageService.getMessage(obj);


    }
    /**
     * 注册接口
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, headers = "api-version=1")
    @ResponseBody
    APIResponse register(@RequestBody Object obj) throws Exception {
      return  messageService.register(obj);


    }
    /**
     * 获取TOKEN接口
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST, headers = "api-version=1")
    @ResponseBody
    APIResponse getToken(@RequestBody Object obj) throws Exception {
        return  messageService.getToken(obj);


    }
    /**
     * 登录接口
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, headers = "api-version=1")
    @ResponseBody
    APIResponse login(@RequestBody Object obj) throws Exception {
        return  messageService.login(obj);
    }

    /**
     * 获取个人信息
     */
    @RequestMapping(value = "/loginByGet", method = RequestMethod.GET, headers = "api-version=1")
    @ResponseBody
    APIResponse loginByGet(String access_token) throws Exception {
        return  messageService.loginByGet(access_token);
    }



    @RequestMapping(value = "/quotes", method = RequestMethod.POST, headers = "api-version=1")
     void quotes(@RequestBody Result r) throws Exception {
        System.out.println(r.getRequest().getPackages().get(0).getType()+r.getRequest().getPackages().get(1).getType());
    }

}
