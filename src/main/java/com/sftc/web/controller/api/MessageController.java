package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.Result;
import net.sf.json.JSONObject;
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
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(obj);
        return messageService.getMessage(apiRequest);
    }

    /**
     * 注册接口
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    APIResponse register(@RequestBody Object obj) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(obj);
        return messageService.register(apiRequest);
    }

    /**
     * 获取TOKEN接口
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    APIResponse getToken(@RequestBody Object obj) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(obj);
        return messageService.getToken(apiRequest);
    }

    /**
     * 登录接口
     */
    @RequestMapping(value = "/sfLogin", method = RequestMethod.POST)
    @ResponseBody
    APIResponse login(@RequestBody Object obj) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(obj);
        return messageService.sfLogin(apiRequest);
    }

    @RequestMapping(value = "/quotes", method = RequestMethod.POST)
    void quotes(@RequestBody Result r) throws Exception {
        System.out.println(r.getRequest().getPackages().get(0).getType() + r.getRequest().getPackages().get(1).getType());
    }

    @RequestMapping(value = "/message/captchas", method = RequestMethod.GET)
    @ResponseBody
    APIResponse messagCeaptchas() throws Exception {

        return messageService.captchas();
    }

}
