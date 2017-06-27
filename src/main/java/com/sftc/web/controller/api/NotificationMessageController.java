package com.sftc.web.controller.api;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.service.NotificationMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
}
