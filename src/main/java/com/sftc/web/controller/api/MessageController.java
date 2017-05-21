package com.sftc.web.controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.Order;
import com.sftc.web.model.Result;
import com.sftc.web.model.quotes.Request;
import com.sftc.web.model.quotes.Source;
import jdk.nashorn.api.scripting.JSObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

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
      return  messageService.register(obj);


    }
    /**
     * 获取TOKEN接口
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    Result getToken(@RequestBody Object obj) throws Exception {
        return  messageService.getToken(obj);


    }
    /**
     * 登录接口
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    APIResponse login(@RequestBody Object obj) throws Exception {
        return  messageService.login(obj);
    }
    @RequestMapping(value = "/quotes", method = RequestMethod.POST)
     void quotes(@RequestBody Result r) throws Exception {
        System.out.println(r.getRequest().getPackages().get(0).getType()+r.getRequest().getPackages().get(1).getType());
    }

}
