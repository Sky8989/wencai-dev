package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequest.UserUnpackingVO;
import com.sftc.web.service.UserUnpackingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value = "用户拆包", httpMethod = "POST",notes = "每次都先判断是否已拆（type：0）\n " +
            "再根据返回的true or false 进行拆包（调用相同接口，其中type为：1）")
    @PostMapping(value = "/unpacking")
    public APIResponse unpacking(@RequestBody UserUnpackingVO userUnpackingVO,
                                 HttpServletRequest request){
        APIRequest apiRequest = new APIRequest(request);
        apiRequest.setRequestParam(userUnpackingVO);
       return userUnpackingService.unpacking(apiRequest);
    }




}
