package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.IndexService;
import com.sftc.web.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Api(description = "设置")
@RequestMapping("index")
public class IndexController {

    @Resource
    private IndexService indexService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/environment/setup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupEnvironment(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return indexService.setupEnvironment(request);
    }

    @RequestMapping(value = "/common/token/setup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupCommonToken(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return indexService.setupCommonToken(request);
    }

    //生成临时Token的接口  2017-10-23 xf
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getTemporaryToken() throws Exception {
        return userService.getTemporaryToken();
    }
}
