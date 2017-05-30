package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;

import com.sftc.web.model.Token;

import com.sftc.web.model.reqeustParam.MyOrderParam;

import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.model.sfmodel.Requests;
import com.sftc.web.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @Resource
    private OrderService orderService;

    /**
     * 提交订单接口
     * @param @request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/place", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse placeOrder(@RequestBody Object object) throws Exception {
        return orderService.placeOrder(object);
    }
    /**
     * 计价
     * @param @request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/countPrice", method = RequestMethod.POST ,headers = "api-version=1")
    public @ResponseBody
    APIResponse countPrice(@RequestBody Object object) throws Exception {
        return orderService.countPrice(object);
    }



    /**
     * 好友填写收件接口
     * @param @request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fill", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse fillOrder(@RequestBody OrderExpress orderExpress) throws Exception {
        return orderService.friendFillOrder(orderExpress);
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
     * 我的订单接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAllOrder", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse getAllOrder(HttpServletRequest request) throws Exception {
        return orderService.getAllOrder(new APIRequest(request));
    }

    /**
     * 获取订单详情
     * @param @request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getOrderDetile", method = RequestMethod.GET,headers = "api-version=1")
    public @ResponseBody
    APIResponse getOrderDetile(Order order,OrderExpress orderExpress,Token token) throws Exception {
        return orderService.getOrderDetile(order,orderExpress,token);
    }

    /**
     * 修改订单
     * @param request
     * @param order
     * @param orderExpress
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST,headers = "api-version=1")
    public @ResponseBody
    APIResponse updateOrder(HttpServletRequest request,Order order,OrderExpress orderExpress) throws Exception {

        return orderService.updateOrder(new APIRequest(request), order, orderExpress);

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

    @RequestMapping(value = "/my", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse myOrder(@RequestBody MyOrderParam myOrderParam) throws Exception {
        return orderService.getMyOrderList(myOrderParam);
    }

    /**
     * 好友订单提交
     * * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/friendPlace", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse friendPlace(@RequestBody Object object) throws Exception {
        return orderService.friendPlace(object);
    }

    /**
     * 顺丰详情接口
     * * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sfDetail", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse sfDetail(OrderExpress orderExpress,Token token) throws Exception {

        return orderService.sfOrderDetail(orderExpress.getOrder_id(),token.getAccess_token(),orderExpress.getUuid());
    }

    @RequestMapping(value = "/placeOrderDetail", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse placeOrderDetail(OrderExpress orderExpress,Token token) throws Exception {

        return orderService.placeOrderDetail(orderExpress.getUuid(),token.getAccess_token());
    }
}
