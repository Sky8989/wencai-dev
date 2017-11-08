package com.sftc.web.controller.app;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.UserLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiImplicitParam(name = "user_contact_id", value = "用户好友关系id", required = true, paramType = "query", defaultValue = "156")
    @PostMapping(value = "all")
    public APIResponse getUserAllLabelByUCID(@RequestParam(value = "user_contact_id") int user_contact_id) {
        return userLabelService.getUserAllLabelByUCID(user_contact_id);
    }


    /**
     * 根据用户好友关系id获取用户系统以及自定义标签
     */
    @ApiOperation(value = "好友系统以及自定义标签", httpMethod = "POST")
    @ApiImplicitParam(name = "user_contact_id", value = "用户好友关系id", required = true, paramType = "query", defaultValue = "156")
    @PostMapping(value = "details")
    public APIResponse getUserLabelDetailsByUCID(@RequestParam(value = "user_contact_id") int user_contact_id) {
        return userLabelService.getUserLabelDetailsByUCID(user_contact_id);
    }


    /**
     * 根据标签id修改个人标签
     */
    @ApiOperation(value = "修改个人标签", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "label_id", value = "标签id", required = true, paramType = "query", defaultValue = "8"),
            @ApiImplicitParam(name = "labels", value = "标签数组", required = true, paramType = "query", defaultValue = "['4122','1273891273812','系统标签7']"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, paramType = "query", defaultValue = "0")
    })
    @PostMapping(value = "update")
    public APIResponse updateUsrLabelByLID(@RequestParam(value = "label_id") int label_id, @RequestParam(value = "labels") String labels,@RequestParam(value = "type") int type) {
        return userLabelService.updateUsrLabelByLID(label_id, labels,type);
    }
}
