package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("index")
public class IndexController {

    @Resource
    private IndexService indexService;

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
}
