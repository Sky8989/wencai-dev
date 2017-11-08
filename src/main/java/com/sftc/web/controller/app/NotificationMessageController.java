package com.sftc.web.controller.app;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.service.NotificationMessageService;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "通知")
@RequestMapping("notification")
public class NotificationMessageController extends BaseController {
    @Resource
    private NotificationMessageService notificationMessageService;

    @ApiOperation(value = "未读通知列表",httpMethod = "GET")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse placeOrder(HttpServletRequest request) throws Exception {
        return notificationMessageService.getMessage(new APIRequest(request));
    }

    @ApiOperation(value = "更新通知消息读取状态",httpMethod = "PATCH")
    @RequestMapping(method = RequestMethod.PATCH)
    public @ResponseBody()
    APIResponse updateIsRead(
            @ApiParam(name = "message_id",value = "消息id",required = true,defaultValue = "141")
            @RequestParam("message_id") int id) throws Exception {
        return notificationMessageService.updateMessage(id);
    }
}
