package com.sftc.web.controller.app;

import com.sftc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Api(description = "七牛")
@RequestMapping("qiniu")
public class QiniuController extends BaseController {

    @ApiOperation(value = "获取七牛token",httpMethod = "GET")
    @RequestMapping(value = "/uptoken",method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> uptoken() throws Exception {
        return qiniuService.returnUptoken();
    }
 }
