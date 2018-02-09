package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.vo.swaggerRequest.FriendListVO;
import com.sftc.web.model.entity.UserContactLabel;
import com.sftc.web.model.vo.swaggerRequest.UserContactParamVO;
import com.sftc.web.model.vo.swaggerRequest.FriendStarVO;
import com.sftc.web.service.UserContactLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "好友相关")
@RequestMapping("friend")
public class UserContactController extends BaseController {

    @Resource
    private UserContactLabelService userContactLabelService;

    @ApiOperation(value = "我的好友列表",httpMethod = "POST")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    APIResponse allFriend(@RequestBody FriendListVO friendListVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(friendListVO);
        return userContactService.getFriendList(request);
    }

    @ApiOperation(value = "好友圈来往记录",httpMethod = "POST")
    @RequestMapping(value = "/contacts", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getContactInfo(@RequestBody UserContactParamVO userContactParamVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(userContactParamVO);
        return userContactService.getContactInfo(request);
    }

    @ApiOperation(value = "好友标星",httpMethod = "PUT")
    @RequestMapping(value = "/star",method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse getContactInfo(@RequestBody FriendStarVO friendStarVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(friendStarVO);
        return userContactService.starFriend(request);
    }

    @ApiOperation(value = "好友详情",httpMethod = "GET")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ApiImplicitParam(name = "friend_id",value = "好友id",required = true,paramType = "query",defaultValue = "10085")
    public @ResponseBody
    APIResponse friendDetail(HttpServletRequest request) throws Exception {
        return userContactService.getFriendDetail(new APIRequest(request));
    }

    @ApiIgnore
    @ApiOperation(value = "添加好友标签",httpMethod = "POST")
    @RequestMapping(value = "/label", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addLabelFriend(@RequestBody UserContactLabel userContactLabel) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userContactLabel);
        return userContactLabelService.addLabelForFriend(apiRequest);
    }

    @ApiIgnore
    @ApiOperation(value = "删除好友标签",httpMethod = "DELETE")
    @RequestMapping(value = "/label", method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deleteLabelFriend(HttpServletRequest request) throws Exception {
        return userContactLabelService.deleteLabelForFriend(new APIRequest(request));
    }

    @ApiIgnore
    @ApiOperation(value = "查看好友标签",httpMethod = "GET")
    @RequestMapping(value = "/label", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getFriendLabelList(HttpServletRequest request) throws Exception {
        return userContactLabelService.selectFriendLabelList(new APIRequest(request));
    }

    @ApiIgnore
    @ApiOperation(value = "更新好友备注与好友图片",httpMethod = "POST")
    @RequestMapping(value = "/notes", method = RequestMethod.POST)
        public @ResponseBody
    APIResponse updateNotes(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return userContactService.updateNotesAndPicture(apiRequest);
    }

}
