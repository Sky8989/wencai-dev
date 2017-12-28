package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.model.vo.swaggerRequest.*;
import com.sftc.web.model.vo.swaggerResponse.SMSMessageRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(description = "顺丰Api，包括登录、注册、获取短信、获取token等")
@RequestMapping("sf")
public class MessageController extends BaseController {

    @IgnoreToken
    @ApiOperation(value = "获取验证码【调顺丰】", httpMethod = "POST", response = SMSMessageRespVO.class)
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse message(@RequestBody SMSMessageRequestVO body) throws Exception {
        return messageService.getMessage(body);
    }

    @ApiOperation(value = "验证验证码【调顺丰】", httpMethod = "POST", notes = "之前获取token的接口，注册的接口和登录的接口合并为这一个接口\n" +
            "只需要传递用户手机信息和验证码信息，邀请信息如果有就传")
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse messageCheck(@RequestBody UserValidateVO body) throws Exception {
        return messageService.messageCheck(body);
    }

    @ApiIgnore
    @IgnoreToken
    @ApiOperation(value = "注册接口【调顺丰】", httpMethod = "POST")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse register(@RequestBody RegisterRequestVO body) throws Exception {
        return messageService.register(body);
    }

    @ApiIgnore
    @IgnoreToken
    @ApiOperation(value = "获取token【调顺丰】", httpMethod = "POST")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse getToken(@RequestBody GetTokenRequestVO body) throws Exception {
        return messageService.getToken(body);
    }

    @ApiIgnore
    @IgnoreToken
    @ApiOperation(value = "sf登录【调顺丰】", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse login(@RequestBody SFLoginRequestVO body) throws Exception {
        return messageService.sfLogin(body);
    }

    @ApiIgnore
    @ApiOperation(value = "获取图片验证码", httpMethod = "GET")
    @ApiImplicitParam(name = "token", value = "用户token", defaultValue = "f9f99534f926c53d8996ba", paramType = "query", required = true)
    @RequestMapping(value = "/message/captchas", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse messagCeaptchas() throws Exception {
        return messageService.captchas();
    }

}
