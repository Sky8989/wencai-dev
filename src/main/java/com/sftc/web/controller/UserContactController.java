package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.FriendListVO;
import com.sftc.web.model.vo.swaggerRequest.FriendStarVO;
import com.sftc.web.model.vo.swaggerRequest.UserContactParamVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Api(description = "好友相关")
@RequestMapping("friend")
public class UserContactController extends BaseController {

    @ApiOperation(value = "我的好友列表", httpMethod = "POST")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse allFriend(@RequestBody FriendListVO body) throws Exception {
        return userContactService.getFriendList(body);
    }

    @ApiOperation(value = "好友圈来往记录", httpMethod = "POST")
    @RequestMapping(value = "/contacts", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse getContactInfo(@RequestBody UserContactParamVO body) throws Exception {
        return userContactService.getContactInfo(body);
    }

    @ApiOperation(value = "好友标星", httpMethod = "PUT")
    @RequestMapping(value = "/star", method = RequestMethod.PUT)
    public @ResponseBody
    ApiResponse getContactInfo(@RequestBody FriendStarVO body) throws Exception {
        return userContactService.starFriend(body);
    }

    @ApiOperation(value = "好友详情", httpMethod = "GET")
    @RequestMapping(value = "/detail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse friendDetail(@ApiParam(name = "friend_uuid", value = "friend uuid", required = true, defaultValue = "2c9a85895f13a8cc015f19822f6a1437")
                             @RequestParam(value = "friend_uuid") String friendUUId) throws Exception {
        return userContactService.getFriendDetail(friendUUId);
    }

}
