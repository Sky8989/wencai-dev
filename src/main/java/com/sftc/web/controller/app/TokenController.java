package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.common.ControllerHelper;
import com.sftc.web.controller.BaseController;
import com.sftc.web.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("token")
public class TokenController extends BaseController {
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse login(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return tokenService.token(apiRequest);
    }

    @RequestMapping(value = "/token2", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<APIResponse> login2(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        APIResponse apiResponse = APIUtil.paramErrorResponse("测试错误");
        int i = 1/0;
        return ControllerHelper.responseEntityBuilder(apiResponse);

    }

}
