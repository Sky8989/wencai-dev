package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.others.QinniuUptoken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(description = "七牛")
@RequestMapping("qiniu")
public class QiniuController extends BaseController {

    @ApiOperation(value = "获取七牛token", httpMethod = "GET", response = QinniuUptoken.class)
    @RequestMapping(value = "/uptoken", method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse uptoken() throws Exception {
        return qiniuService.returnUptoken();
    }
}
