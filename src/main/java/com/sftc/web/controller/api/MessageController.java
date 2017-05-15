package com.sftc.web.controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sftc.web.controller.AbstractBasicController;
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

    @RequestMapping(value = "/message", method = RequestMethod.POST)

    void message(@RequestBody Object obj) throws Exception {
        messageService.getMessage(obj);


    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)

    void register(@RequestBody Object obj) throws Exception {
        messageService.register(obj);


    }
}
