package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.vo.swaggerRequest.DeleteTokenVO;
import com.sftc.web.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@ApiIgnore
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
}
