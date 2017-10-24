package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(description = "顺丰API，包括登录、注册、获取短信、获取token等")
@RequestMapping("sf")
public class MessageController extends BaseController {

//    @IgnoreToken
    @ApiOperation(value = "获取短信接口", httpMethod = "POST")
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseBody
    APIResponse message(@RequestBody Object obj) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(obj);
        return messageService.getMessage(apiRequest);
    }

//    @IgnoreToken
    @ApiOperation(value = "注册接口", httpMethod = "POST")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    APIResponse register(@RequestBody Object obj) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(obj);
        return messageService.register(apiRequest);
    }

//    @IgnoreToken
    @ApiOperation(value = "获取token接口", httpMethod = "POST")
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    APIResponse getToken(@RequestBody Object obj) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(obj);
        return messageService.getToken(apiRequest);
    }

//    @IgnoreToken
    @ApiOperation(value = "登录接口", httpMethod = "POST")
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

    @ApiOperation(value = "获取图片验证码", httpMethod = "GET")
    @RequestMapping(value = "/message/captchas", method = RequestMethod.GET)
    @ResponseBody
    APIResponse messagCeaptchas() throws Exception {
        return messageService.captchas();
    }

}
