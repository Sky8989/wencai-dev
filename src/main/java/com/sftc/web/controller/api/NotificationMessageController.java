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

/**
 * Created by huxingyue on 2017/6/27.
 */

@Controller
@RequestMapping("notificationMessage")
public class NotificationMessageController extends AbstractBasicController {
    @Resource
    NotificationMessageService notificationMessageService;
    /**
     * 获取通知信息
     */
    @RequestMapping(value = "/getMessage", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse placeOrder(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return notificationMessageService.getMessage(request);
    }
}
