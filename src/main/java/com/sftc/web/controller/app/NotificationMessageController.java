package com.sftc.web.controller.app;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.SwaggerRequestVO.UpdateIsReadVO;
import com.sftc.web.service.NotificationMessageService;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "通知")
@RequestMapping("notification")
public class NotificationMessageController extends BaseController {
    @Resource
    private NotificationMessageService notificationMessageService;

    @ApiOperation(value = "未读通知列表",httpMethod = "GET",notes = "一是作为寄件人，好友填了包裹地址时收到的，包含订单信息；\n" +
            "二是作为收件人，收到他人寄的包裹时收到的。")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse placeOrder(HttpServletRequest request) throws Exception {
        return notificationMessageService.getMessage(new APIRequest(request));
    }

    @ApiOperation(value = "更新通知消息读取状态",httpMethod = "PUT")
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody()
    APIResponse updateIsRead(@RequestBody UpdateIsReadVO updateIsReadVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(updateIsReadVO);
        return notificationMessageService.updateMessage(apiRequest);
    }
}
