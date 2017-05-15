package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
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
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/place", method = RequestMethod.POST, headers = "api-version=2")
    public @ResponseBody
    APIResponse friendPlaceOrder(HttpServletRequest request) throws Exception {
        return orderService.friendPlaceOrder(new APIRequest(request));
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

    /*
    * 我的订单接口
    * @param request
    * @return
    *
    * */
    @RequestMapping(value = "/getAllOrder", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse getAllOrder(HttpServletRequest request) throws Exception {
        return orderService.getAllOrder(new APIRequest(request));
    }
    /*
    * 获取订单详情
    * */
    @RequestMapping(value = "/getOrderDetile", method = RequestMethod.POST,headers = "api-version=1")
    public @ResponseBody
    APIResponse getOrderDetile(HttpServletRequest request) throws Exception {
        return orderService.getOrderDetile(new APIRequest(request));
    }
    /*
   * 修改订单
   * */
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST,headers = "api-version=1")
    public @ResponseBody
    APIResponse updateOrder(HttpServletRequest request,Order order,OrderExpress orderExpress) throws Exception {

        return orderService.updateOrder(new APIRequest(request),order,orderExpress);

    }
}
