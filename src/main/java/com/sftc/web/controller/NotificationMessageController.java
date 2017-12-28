package com.sftc.web.controller;


import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.UpdateIsReadVO;
import com.sftc.web.model.vo.swaggerResponse.NotificaionMessageListVO;
import com.sftc.web.model.vo.swaggerResponse.ResponseMessageVO;
import com.sftc.web.service.NotificationMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Api(description = "通知")
@RequestMapping("notification")
public class NotificationMessageController extends BaseController {
    @Resource
    private NotificationMessageService notificationMessageService;

    @ApiOperation(value = "未读通知列表", httpMethod = "GET", notes = "一是作为寄件人，好友填了包裹地址时收到的，包含订单信息；\n" +
            "二是作为收件人，收到他人寄的包裹时收到的。", response = NotificaionMessageListVO.class)
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse placeOrder() throws Exception {
        return notificationMessageService.getMessage();
    }

    @ApiOperation(value = "更新通知消息读取状态", httpMethod = "PUT", response = ResponseMessageVO.class)
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody()
    ApiResponse updateIsRead(@RequestBody UpdateIsReadVO body) throws Exception {
        return notificationMessageService.updateMessage(body);
    }
}
