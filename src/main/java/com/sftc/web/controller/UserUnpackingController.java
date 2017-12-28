package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.UserUnpackingVO;
import com.sftc.web.service.UserUnpackingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@RestController
@ApiIgnore
@Api(description = "用户拆包")
@RequestMapping(value = "unpacking")
public class UserUnpackingController {

    @Autowired
    private UserUnpackingService userUnpackingService;

    /**
     * 根据订单id和token拆包
     */
    @ApiOperation(value = "用户拆包", httpMethod = "POST", notes = "每次都先判断是否已拆（type：0）\n " +
            "再根据返回的true or false 进行拆包（调用相同接口，其中type为：1）")
    @PostMapping(value = "/unpacking")
    public ApiResponse unpacking(@RequestBody UserUnpackingVO body,
                                 HttpServletRequest request) {
        return userUnpackingService.unpacking(body, request);
    }


}
