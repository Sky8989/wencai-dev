package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.controller.BaseController;
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
@Api(description = "顺丰API，包括登录、注册、获取短信、获取token等")
@RequestMapping("sf")
public class MessageController extends BaseController {

    @IgnoreToken
    @ApiOperation(value = "获取验证码【调顺丰】", httpMethod = "POST",response = SMSMessageRespVO.class)
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse message(@RequestBody SMSMessageRequestVO smsMessageRequestVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(smsMessageRequestVO);
        return messageService.getMessage(apiRequest);
    }

    @ApiOperation(value = "验证验证码【调顺丰】", httpMethod = "POST",notes = "之前获取token的接口，注册的接口和登录的接口合并为这一个接口\n"+
    "只需要传递用户手机信息和验证码信息，邀请信息如果有就传")
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse messageCheck(@RequestBody UserValidateVO userValidateVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userValidateVO);
        return messageService.messageCheck(apiRequest);
    }
}
