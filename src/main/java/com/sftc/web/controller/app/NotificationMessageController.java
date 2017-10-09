package com.sftc.web.controller.app;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.service.NotificationMessageService;
import io.swagger.annotations.Api;
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

    /**
     * 获取通知信息
     */
    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse placeOrder(HttpServletRequest request) throws Exception {
        return notificationMessageService.getMessage(new APIRequest(request));
    }

    /**
     * 更新通知消息的is_read状态
     */
    @RequestMapping(value = "/updateIsRead", method = RequestMethod.GET)
    public @ResponseBody()
    APIResponse updateIsRead(@RequestParam("message_id") int id) throws Exception {
        return notificationMessageService.updateMessage(id);
    }
}
