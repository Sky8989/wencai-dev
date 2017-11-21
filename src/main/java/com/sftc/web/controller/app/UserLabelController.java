package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequest.UpdateUserContactLabelVO;
import com.sftc.web.model.vo.swaggerRequest.UserLabelVO;
import com.sftc.web.service.UserLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(description = "用户标签")
@RestController
@RequestMapping(value = "label")
public class UserLabelController {

    @Resource
    private UserLabelService userLabelService;

    /**
     * 根据用户好友关系id获取用户所有标签
     */
    @ApiOperation(value = "好友所有标签", httpMethod = "POST")
    @PostMapping(value = "/all")
    public APIResponse getUserAllLabelByUCID(@RequestBody UserLabelVO userLabelVO) {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userLabelVO);
        return userLabelService.getUserContactLabels(apiRequest);
    }

    /**
     * 根据标签id修改个人标签
     */
    @ApiOperation(value = "修改个人标签", httpMethod = "POST")
    @PostMapping(value = "update")
    public APIResponse updateUsrLabelByLID(@RequestBody UpdateUserContactLabelVO updateUserContactLabelVO) {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(updateUserContactLabelVO);
        return userLabelService.updateUserContactLabels(apiRequest);
    }
}
