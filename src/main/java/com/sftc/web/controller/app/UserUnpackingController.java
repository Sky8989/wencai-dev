package com.sftc.web.controller.app;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.UserUnpackingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(description = "用户拆包")
@RequestMapping(value = "unpacking")
public class UserUnpackingController {

    @Autowired
    private UserUnpackingService userUnpackingService;
    /**
     * 根据订单id和token拆包
     */
    @ApiOperation(value = "用户拆包", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单id", required = true, paramType = "String", defaultValue = "1"),
            @ApiImplicitParam(name = "type", value = "0：判断是否拆包/1：进行拆包", required = true, paramType = "int", defaultValue = "1/0")
    })
    @PostMapping(value = "unpacking")
    public APIResponse unpacking(@RequestParam(value = "order_id")String order_id,
                                 @RequestParam(value = "type")int type,
                                 HttpServletRequest request){
       return userUnpackingService.unpacking(order_id,request,type);
    }




}
