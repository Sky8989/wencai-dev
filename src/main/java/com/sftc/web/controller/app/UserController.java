package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.vo.swaggerRequest.UserMerchantsRequestVO;
import com.sftc.web.model.vo.swaggerRequest.UserParamVO;
import com.sftc.web.model.vo.swaggerResponse.ResponseMessageVO;
import com.sftc.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@RequestMapping("user")
@Api(description = "用户相关")
@Controller
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @IgnoreToken
    @ApiOperation(value = "微信登录获取open_id", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 401,message = "The query fails"),
            @ApiResponse(code = 402,message = "The submit fails"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse login(@RequestBody UserParamVO userParamVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(userParamVO);
        return userService.login(request);
    }

    @IgnoreToken
    @ApiOperation(value = "超级登录", httpMethod = "POST",notes = "通过jscode，获取登陆信息，以及access_token和uuid，并且在旧的token失效时自动刷新。\n" +
            "如果用户没有access_token或者access_token失效，则不会传递access_token和uuid，以此区分access_token是否有效。")
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 401,message = "The query fails"),
            @ApiResponse(code = 402,message = "The submit fails"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    @RequestMapping(value = "/login/pro", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse superLogin(@RequestBody UserParamVO userParamVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(userParamVO);
        return userService.superLogin(request);
    }

    //10-12日提出的新需求 更新个人信息
    @ApiOperation(value = "更新个人信息", httpMethod = "PUT",notes = "更新用户信息,2017-10-12的新需求，前端需要在下单前调用",response = ResponseMessageVO.class)
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    @RequestMapping(value = "/merchants/me", method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse updatePersonMessage(@RequestBody UserMerchantsRequestVO userMerchantsRequestVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userMerchantsRequestVO);
        return userService.updatePersonMessage(apiRequest);
    }
}
