package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.Token;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.service.EvaluateService;
import com.sftc.web.service.OrderExpressService;
import com.sftc.web.service.OrderService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController extends AbstractBasicController {

    @Resource
    private OrderService orderService;
    @Resource
    private EvaluateService evaluateService;

    /**
     * 普通订单提交
     */
    @RequestMapping(value = "/normalCommit", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse placeOrder(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.normalOrderCommit(request);
    }

    /**
     * 好友订单提交
     */
    @RequestMapping(value = "/friendCommit", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendOrderCommit(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.friendOrderCommit(request);
    }

    /**
     * 计价
     */
    @RequestMapping(value = "/countPrice", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse countPrice(@RequestBody Object object) throws Exception {
        return orderService.countPrice(object);
    }

    /**
     * 好友填写收件
     */
    @RequestMapping(value = "/fill", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse fillOrder(@RequestBody Map rowData) throws Exception {
        return orderService.friendFillOrder(rowData);
    }

    /**
     * 寄件人填写订单
     */
    @RequestMapping(value = "/senderplace", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendPlaceOrder(@RequestBody OrderParam orderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderParam);
        return orderService.friendPlaceOrder(request);
    }

    /**
     * 支付订单
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse payOrder(HttpServletRequest request) throws Exception {
        return orderService.payOrder(new APIRequest(request));
    }

    /**
     * 根据订单编号查找未被填写包裹的接口
     */
    @RequestMapping(value = "/pack", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse pack(HttpServletRequest request) throws Exception {
        return orderService.getEmptyPackage(new APIRequest(request));
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse detail(HttpServletRequest request) throws Exception {
        return orderService.selectOrderDetail(new APIRequest(request));
    }

    /**
     * 我的订单列表
     */
    @RequestMapping(value = "/my", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse myOrder(@RequestBody MyOrderParam myOrderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(myOrderParam);
        return orderService.getMyOrderList(request);
    }

    /**
     * 我的好友订单列表
     */
    @RequestMapping(value = "/friendCircle", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendOrder(@RequestBody MyOrderParam myOrderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(myOrderParam);
        return orderService.getMyFriendCircleOrderList(request);
    }

    /**
     * 好友订单提交
     */
    @RequestMapping(value = "/friendPlace", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendPlace(@RequestBody Object object) throws Exception {
        return orderService.friendPlace(object);
    }

    /**
     * 顺丰详情接口，查询快递实时状态
     */
    @RequestMapping(value = "/sfDetail", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse sfDetail(OrderExpress orderExpress, Token token) throws Exception {
        return orderService.sfOrderDetail(orderExpress.getOrder_id(), token.getAccess_token(), orderExpress.getUuid());
    }

    /**
     * 快递详情
     */
    @RequestMapping(value = "/expressDetail", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse placeOrderDetail(HttpServletRequest request) throws Exception {
        return orderService.selectExpressDetail(new APIRequest(request));
    }

    /**
     * 更改订单状态
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse updateOrderStatus(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.updateOrderStatus(request);
    }

    /**
     * 未下单详情
     */
    @RequestMapping(value = "/noPlaceDetail", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse noPlaceDetail(OrderExpress orderExpress) throws Exception {
        return orderService.noPlaceOrderDetail(orderExpress.getOrder_id());
    }

    /**
     * 评价小哥（评价顺丰订单）
     */
    @RequestMapping(value = "/evaluateSingle", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse evaluate(@RequestBody Object object) throws Exception {
        return orderService.evaluateSingle(object);
    }

    /**
     * 取消订单
     */
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse cancelOrder(@RequestBody Object object) throws Exception {
//        return orderService.deleteOrder(object);
        return orderService.cancelOrder(object);
    }

    /**
     * 时间规则
     */
    @RequestMapping(value = "/constants", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse constants(HttpServletRequest request) throws Exception {
        return orderService.timeConstants(new APIRequest(request));
    }

    /**
     * 大网计价
     */
    @RequestMapping(value = "/OrderFreightQuery", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse OrderFreightQuery(@RequestBody Object object) throws Exception {
        return orderService.OrderFreightQuery(object);
    }

    /**
     * 大网路由
     */
    @RequestMapping(value = "/OrderRouteQuery", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse OrderRouteQuery(HttpServletRequest request) throws Exception {
        return orderService.OrderRouteQuery(new APIRequest(request));
    }

    /**
     * 寄件人可下单的神秘订单列表
     */
    @RequestMapping(value = "/remindPlace", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse remindPlace(HttpServletRequest request) throws Exception {
        return orderService.remindPlace(new APIRequest(request));
    }

    /**
     * 给顺丰调用的，同城单转大网单
     */
    @RequestMapping(value = "/transform", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse transformOrder(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.transformOrderFromSameToNation(request);
    }

    /**
     * 获取 大网订单的评价信息
     */
    @RequestMapping(value = "/getEvaluate", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getEvaluate(String uuid) throws Exception {
        return evaluateService.getEvaluate(uuid);
    }

    /**
     * 设置大网预约单定时器
     */
    @RequestMapping(value = "/reserve/setup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupReserveTimer(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.setupReserveNationOrderCommitTimer(request);
    }

    /**
     * 设置大网预约单定时器
     */
    @RequestMapping(value = "/cancel/setup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupCancelNationOrderTimer(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.setupCancelNationOrderTimer(request);
    }

    /**
     * 设置兜底记录已读
     */
    @RequestMapping(value = "/transform/read", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse readExpressTransform(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.readExpressTransform(request);
    }

}
