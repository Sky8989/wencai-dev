package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.OrderCancel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/5/14.
 */
@Controller
@RequestMapping("orderCancel")
public class OrderCancelController extends AbstractBasicController{
    @RequestMapping(value = "/deleOrder", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse placeOrder(OrderCancel orderCancel) throws Exception {
        return orderCancelService.deleteOrder(orderCancel);
    }
}
