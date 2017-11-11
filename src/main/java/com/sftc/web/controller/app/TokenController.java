package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.SwaggerRequestVO.DeleteTokenVO;
import com.sftc.web.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@RequestMapping("token")
@Api(description = "token")
@Controller
public class TokenController extends BaseController {
    @Resource
    private TokenService tokenService;

    @IgnoreToken
    @ApiOperation(value = "删除token表整行数据/前端请除token使用", httpMethod = "DELETE")
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deleteToken(@RequestBody DeleteTokenVO deleteTokenVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(deleteTokenVO);
        return tokenService.deleteToken(apiRequest);
    }

//    @RequestMapping(value = "/token2", method = RequestMethod.POST)
//    public @ResponseBody
//    ResponseEntity<APIResponse> login2(@RequestBody Object object) throws Exception {
//        APIRequest apiRequest = new APIRequest();
//        apiRequest.setRequestParam(object);
//        APIResponse apiResponse = APIUtil.paramErrorResponse("测试错误");
//        int i = 1/0;
//        return ControllerHelper.responseEntityBuilder(apiResponse);
//    }

}
