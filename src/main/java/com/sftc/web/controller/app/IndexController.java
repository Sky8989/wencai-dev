package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.model.vo.swaggerRequestVO.SFAPIRequestVO;
import com.sftc.web.model.vo.swaggerRequestVO.SFAccessTokenRequestVO;
import com.sftc.web.service.IndexService;
import com.sftc.web.service.UserService;
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
@Controller
@Api(description = "设置")
@RequestMapping("index")
public class IndexController {

    @Resource
    private IndexService indexService;
    @Resource
    private UserService userService;

    @ApiOperation(value = "设置顺丰专送API环境", httpMethod = "POST",notes = "设置项目对接的顺丰同城的API环境，项目部署后默认是dev环境")
    @RequestMapping(value = "/environment", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupEnvironment(@RequestBody SFAPIRequestVO sfapiRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(sfapiRequestVO);
        return indexService.setupEnvironment(request);
    }

    @ApiOperation(value = "设置顺丰专送公共token", httpMethod = "POST")
    @RequestMapping(value = "/common/token", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupCommonToken(@RequestBody SFAccessTokenRequestVO sfAccessTokenRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(sfAccessTokenRequestVO);
        return indexService.setupCommonToken(request);
    }

    @IgnoreToken
    @ApiOperation(value = "生成临时token接口", httpMethod = "POST",notes = "获取临时Token给顺丰调用，兜底时使用")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getTemporaryToken() throws Exception {
        return userService.getTemporaryToken();
    }
}
