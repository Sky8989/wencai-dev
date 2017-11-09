package com.sftc.web.controller.app;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.UserUnpackingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;

@RestController
@Api(description = "用户拆包")
@RequestMapping(value = "unpacking")
public class UserUnpackingController {

    @Autowired
    private UserUnpackingService userUnpackingService;
    /**
     * 根据订单id和token拆包
     */
    @ApiOperation(value = "用户拆包", httpMethod = "POST",notes = "每次都先判断是否已拆（type：0）\n " +
            "再根据返回的true or false 进行拆包（调用相同接口，其中type为：1）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单id", required = true, paramType = "form", defaultValue = "1"),
            @ApiImplicitParam(name = "type", value = "0：判断是否拆包/1：进行拆包", required = true, paramType = "form", defaultValue = "1/0")
    })
    @PostMapping(value = "unpacking",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public APIResponse unpacking(@RequestParam(value = "order_id")String order_id,
                                 @RequestParam(value = "type")int type,
                                 HttpServletRequest request){
       return userUnpackingService.unpacking(order_id,request,type);
    }




}
