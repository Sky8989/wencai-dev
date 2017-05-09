package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description: 订单接口
 * @date 2017/4/29
 * @Time 上午1:27
 */
@Controller
@RequestMapping("order")
public class OrderController extends AbstractBasicController {

    @RequestMapping(value = "/place", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse placeOrder(HttpServletRequest request) throws Exception {
        return orderService.placeOrder(new APIRequest(request));
    }
}