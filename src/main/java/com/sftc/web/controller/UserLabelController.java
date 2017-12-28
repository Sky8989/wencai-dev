package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.UpdateUserContactLabelVO;
import com.sftc.web.model.vo.swaggerRequest.UserLabelVO;
import com.sftc.web.service.UserLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@ApiIgnore
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
    public ApiResponse getUserAllLabelByUCID(@RequestBody UserLabelVO body) {
        return userLabelService.getUserContactLabels(body);
    }

    /**
     * 根据标签id修改个人标签
     */
    @ApiOperation(value = "修改个人标签", httpMethod = "POST")
    @PostMapping(value = "update")
    public ApiResponse updateUsrLabelByLID(@RequestBody UpdateUserContactLabelVO body) {
        return userLabelService.updateUserContactLabels(body);
    }

}
