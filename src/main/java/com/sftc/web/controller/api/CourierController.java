package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/5/13.
 */
@Controller
@RequestMapping("courier")
public class CourierController extends AbstractBasicController{
    /**
     * 提交订单接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getCourier", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse placeOrder(HttpServletRequest request) throws Exception {
        return courierService.getCourier(new APIRequest(request));
    }
}
