package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.Result;
import com.sftc.web.model.SwaggerRequestVO.GetTokenRequestVO;
import com.sftc.web.model.SwaggerRequestVO.RegisterRequestVO;
import com.sftc.web.model.SwaggerRequestVO.SFLoginRequestVO;
import com.sftc.web.model.SwaggerRequestVO.SMSMessageRequestVO;
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
@Api(description = "顺丰API，包括登录、注册、获取短信、获取token等")
@RequestMapping("sf")
public class MessageController extends BaseController {

    @IgnoreToken
    @ApiOperation(value = "获取验证码【调顺丰】", httpMethod = "POST")
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse message(@RequestBody SMSMessageRequestVO smsMessageRequestVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(smsMessageRequestVO);
        return messageService.getMessage(apiRequest);
    }

    @IgnoreToken
    @ApiOperation(value = "注册接口【调顺丰】", httpMethod = "POST")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse register(@RequestBody RegisterRequestVO registerRequestVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(registerRequestVO);
        return messageService.register(apiRequest);
    }

    @IgnoreToken
    @ApiOperation(value = "获取token【调顺丰】", httpMethod = "POST")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse getToken(@RequestBody GetTokenRequestVO getTokenRequestVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(getTokenRequestVO);
        return messageService.getToken(apiRequest);
    }

    @IgnoreToken
    @ApiOperation(value = "sf登录【调顺丰】", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse login(@RequestBody SFLoginRequestVO sfLoginRequestVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(sfLoginRequestVO);
        return messageService.sfLogin(apiRequest);
    }

    @ApiIgnore
    @ApiOperation(value = "获取图片验证码", httpMethod = "GET")
    @ApiImplicitParam(name = "token",value = "用户token",defaultValue = "f9f99534f926c53d8996ba",paramType = "query",required = true)
    @RequestMapping(value = "/message/captchas", method = RequestMethod.GET)
    @ResponseBody
    public APIResponse messagCeaptchas() throws Exception {
        return messageService.captchas();
    }

}
