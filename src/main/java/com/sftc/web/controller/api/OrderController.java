package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.reqeustParam.OrderParam;
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
@RequestMapping("order")
@Controller
public class OrderController extends AbstractBasicController {

    /**
     * 提交订单接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/place", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse placeOrder(HttpServletRequest request) throws Exception {
        return orderService.placeOrder(new APIRequest(request));
    }

    /**
     * 好友填写收件接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fill", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse fillOrder(HttpServletRequest request) throws Exception {
        return orderService.friendFillOrder(new APIRequest(request));
    }

    /**
     * 寄件给好友提交订单接口
     * @param orderParam
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/place", method = RequestMethod.POST, headers = "api-version=2")
    public @ResponseBody
    APIResponse friendPlaceOrder(@RequestBody OrderParam orderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderParam);
        return orderService.friendPlaceOrder(request);
    }

    /**
     * 支付订单接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse payOrder(HttpServletRequest request) throws Exception {
        return orderService.payOrder(new APIRequest(request));
    }

    /**
     * 根据订单编号查找未被填写包裹的接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pack", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse pack(HttpServletRequest request) throws Exception {
        return orderService.getEmptyPackage(new APIRequest(request));
    }
}
