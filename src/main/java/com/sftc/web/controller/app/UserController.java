package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.SwaggerRequestVO.UserMerchantsRequestVO;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.SwaggerRequestVO.UserNewMobileVO;
import com.sftc.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("user")
@Api(description = "用户相关")
@Controller
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @IgnoreToken
    @ApiOperation(value = "微信登录获取open_id", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse login(@RequestBody UserParam userParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(userParam);
        return userService.login(request);
    }

    @IgnoreToken
    @ApiOperation(value = "超级登录", httpMethod = "POST",notes = "通过jscode，获取登陆信息，以及access_token和uuid，并且在旧的token失效时自动刷新。\n" +
            "如果用户没有access_token或者access_token失效，则不会传递access_token和uuid，以此区分access_token是否有效。")
    @RequestMapping(value = "/login/pro", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse superLogin(@RequestBody UserParam userParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(userParam);
        return userService.superLogin(request);
    }

    @IgnoreToken
    @ApiOperation(value = "解除绑定手机", httpMethod = "PUT",notes = "解除绑定手机号，用户登陆小程序，使用原有微信号，解除旧有绑定的手机号。\n" +
            "前端确认用户操作后，传递用户id、用户旧手机号，传递到接口。")
    @RequestMapping(value = "/mobile/unbind", method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse commonUnbind(HttpServletRequest request) throws Exception {
        return userService.deleteMobile(new APIRequest(request));
    }

    @IgnoreToken
    @ApiOperation(value = "修改绑定手机", httpMethod = "PUT")
    @RequestMapping(value = "/mobile/bind", method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse bindNewMobile(@RequestBody UserNewMobileVO userNewMobileVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userNewMobileVO);
        return userService.updateMobile(apiRequest);
    }

    //10-12日提出的新需求 更新个人信息
    @ApiOperation(value = "更新个人信息", httpMethod = "PUT",notes = "更新用户信息,2017-10-12的新需求，前端需要在下单前调用")
    @RequestMapping(value = "/merchants/me", method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse updatePersonMessage(@RequestBody UserMerchantsRequestVO userMerchantsRequestVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userMerchantsRequestVO);
        return userService.updatePersonMessage(apiRequest);
    }
}
