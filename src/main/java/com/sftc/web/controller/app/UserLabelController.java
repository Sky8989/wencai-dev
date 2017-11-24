package com.sftc.web.controller.app;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequest.UpdateUserContactLabelVO;
import com.sftc.web.model.vo.swaggerRequest.UserLabelVO;
import com.sftc.web.model.vo.swaggerRequest.AddUserContactLabelVO;
import com.sftc.web.model.vo.swaggerRequest.DeleteUserContactLabelVo;
import com.sftc.web.service.UserLabelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "CMS用户标签")
@RestController
@RequestMapping(value = "label")
public class UserLabelController {

    @Resource
    private UserLabelService userLabelService;
  
    /**
     * 根据用户好友关系id获取用户所有标签
     */
    @ApiOperation(value = "CMS好友所有标签", httpMethod = "POST")
    @PostMapping(value = "/all")
    public APIResponse getUserAllLabelByUCID(@RequestBody UserLabelVO userLabelVO) {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userLabelVO);
        return userLabelService.getUserContactLabels(apiRequest);
    }

    /**
     * 根据标签id修改个人标签
     */
    @ApiOperation(value = "CMS修改个人标签", httpMethod = "POST")
    @PostMapping(value = "update")
    public APIResponse updateUsrLabelByLID(@RequestBody UpdateUserContactLabelVO updateUserContactLabelVO) {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(updateUserContactLabelVO);
        return userLabelService.updateUserContactLabels(apiRequest);
    }
    /**
     * 新增个人标签
     */
    @ApiOperation(value = "CMS新增个人标签", httpMethod = "POST")
    @PostMapping(value = "add")
    
    public APIResponse addUserLabel(@RequestBody AddUserContactLabelVO addUserContactLabelVO) {
    	APIRequest apiRequest = new APIRequest();
    	apiRequest.setRequestParam(addUserContactLabelVO);
    	return userLabelService.addUserContactLabels(apiRequest);
    }
    /**
     * 删除个人标签
     */
    @ApiOperation(value = "CMS删除个人标签", httpMethod = "DELETE")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public APIResponse deleteUsrLabelByLID(@RequestBody DeleteUserContactLabelVo userLabel) {
    	APIRequest apiRequest = new APIRequest();
    	apiRequest.setRequestParam(userLabel);
    	return userLabelService.deleteUserContactLabels(apiRequest);
    }
}
