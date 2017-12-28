package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.model.vo.swaggerRequest.UserMerchantsRequestVO;
import com.sftc.web.model.vo.swaggerRequest.UserParamVO;
import com.sftc.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.ws.rs.HttpMethod;

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
    ApiResponse login(@RequestBody UserParamVO body) throws Exception {
        return userService.login(body);
    }

    @IgnoreToken
    @ApiOperation(value = "超级登录", httpMethod = "POST", notes = "通过jscode，获取登陆信息，以及access_token和uuid，并且在旧的token失效时自动刷新。\n" +
            "如果用户没有access_token或者access_token失效，则不会传递access_token和uuid，以此区分access_token是否有效。")
    @RequestMapping(value = "/login/pro", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse superLogin(@RequestBody UserParamVO body) throws Exception {
        return userService.superLogin(body);
    }

    @ApiOperation(value = "检查手机绑定状态", httpMethod = "GET", notes = "在下单、兑换优惠券前，都需要检查一下账号是否已经绑定手机号。如果未绑定手机号，需要先绑定（手机号+验证码的方式），再进行后续操作。")
    @RequestMapping(value = "/mobile/bind", method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse checkMobileBindStatus() throws Exception {
        return userService.checkBindStatus();
    }


    @ApiOperation(value = "更新商户信息", httpMethod = "PUT", notes = "更新用户信息, 2017-10-12的新需求，前端需要在下单前调用")
    @RequestMapping(value = "/merchants/me", method = RequestMethod.PUT)
    public @ResponseBody
    ApiResponse updatePersonMessage(@RequestBody UserMerchantsRequestVO body) throws Exception {
        return userService.updatePersonMessage(body);
    }

    @ResponseBody
    @GetMapping(value = "wallets")
    @ApiOperation(value = "获取用户钱包", httpMethod = HttpMethod.GET)
    public ApiResponse obtainUserWallets() {
        return userService.obtainUserWallets(2);
    }

    @ResponseBody
    @GetMapping(value = "balance/detailed/{limit}/{offset}")
    @ApiOperation(value = "获取余额明细", httpMethod = HttpMethod.GET)
    public ApiResponse obtainBalanceDetailed(@PathVariable int limit, @PathVariable int offset) {
        return userService.obtainBalanceDetailed(limit, offset);
    }
}
