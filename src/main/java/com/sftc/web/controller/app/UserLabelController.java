package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.SwaggerRequestVO.UpdateUsrLabelVO;
import com.sftc.web.model.SwaggerRequestVO.UserLabelVO;
import com.sftc.web.service.UserLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(description = "用户标签")
@RestController
@RequestMapping(value = "label")
public class UserLabelController {

    @Autowired
    private UserLabelService userLabelService;

    /**
     * 根据用户好友关系id获取用户所有标签
     */
    @ApiOperation(value = "好友所有标签", httpMethod = "POST")
    @PostMapping(value = "/all")
    public APIResponse getUserAllLabelByUCID(@RequestBody UserLabelVO userLabelVO) {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userLabelVO);
        return userLabelService.getUserAllLabelByUCID(apiRequest);
    }


    /**
     * 根据用户好友关系id获取用户系统以及自定义标签
     */
    @ApiOperation(value = "好友系统以及自定义标签", httpMethod = "POST")
    @PostMapping(value = "/details")
    public APIResponse getUserLabelDetailsByUCID(@RequestBody UserLabelVO userLabelVO) {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userLabelVO);
        return userLabelService.getUserLabelDetailsByUCID(apiRequest);
    }


    /**
     * 根据标签id修改个人标签
     */
    @ApiOperation(value = "修改个人标签", httpMethod = "POST")
    @PostMapping(value = "update")
    public APIResponse updateUsrLabelByLID(@RequestBody UpdateUsrLabelVO updateUsrLabelVO) {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(updateUsrLabelVO);
        return userLabelService.updateUsrLabelByLID(apiRequest);
    }
}
