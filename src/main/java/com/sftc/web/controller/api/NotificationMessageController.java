package com.sftc.web.controller.api;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.service.NotificationMessageService;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("notification")
public class NotificationMessageController extends AbstractBasicController {
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
