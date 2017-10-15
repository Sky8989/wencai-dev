package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.service.EvaluateService;
import com.sftc.web.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@Api(description = "订单相关接口")
@RequestMapping("order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;
    @Resource
    private EvaluateService evaluateService;

    @ApiOperation(value = "普通订单提交",httpMethod = "POST")
    @RequestMapping(value = "/normalCommit", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse placeOrder(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.addNormalOrderCommit(request);
    }

    @ApiOperation(value = "好友订单提交",httpMethod = "POST")
    @RequestMapping(value = "/friendCommit", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendOrderCommit(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.addFriendOrderCommit(request);
    }

    @ApiOperation(value = "计价接口",httpMethod = "POST")
    @RequestMapping(value = "/countPrice", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse countPrice(@RequestBody Object object) throws Exception {
        return orderService.countPrice(object);
    }

    @ApiOperation(value = "好友填写收件",httpMethod = "POST")
    @RequestMapping(value = "/fill", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse fillOrder(@RequestBody Map rowData) throws Exception {
        return orderService.friendFillOrder(rowData);
    }

    @ApiOperation(value = "寄件人填写订单",httpMethod = "POST")
    @RequestMapping(value = "/senderplace", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendPlaceOrder(@RequestBody OrderParam orderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderParam);
        return orderService.friendPlaceOrder(request);
    }

    @ApiOperation(value = "支付订单",httpMethod = "POST")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse payOrder(HttpServletRequest request) throws Exception {
        return orderService.payOrder(new APIRequest(request));
    }

    @ApiOperation(value = "订单详情",httpMethod = "GET")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return orderService.selectOrderDetail(new APIRequest(request));
    }

    @ApiOperation(value = "我的订单列表",httpMethod = "POST")
    @RequestMapping(value = "/my", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse myOrder(@RequestBody MyOrderParam myOrderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(myOrderParam);
        return orderService.getMyOrderList(request);
    }

    @ApiOperation(value = "我的好友订单列表",httpMethod = "POST")
    @RequestMapping(value = "/friendCircle", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendOrder(@RequestBody MyOrderParam myOrderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(myOrderParam);
        return orderService.getMyFriendCircleOrderList(request);
    }

    @IgnoreToken
    @ApiOperation(value = "快递详情",httpMethod = "GET")
    @RequestMapping(value = "/expressDetail", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse placeOrderDetail(HttpServletRequest request) throws Exception {
        return orderService.selectExpressDetail(new APIRequest(request));
    }

    @ApiOperation(value = "更改订单状态",httpMethod = "POST")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse updateOrderStatus(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.updateOrderStatus(request);
    }

    @ApiOperation(value = "更改快递状态",httpMethod = "POST")
    @RequestMapping(value = "/updateExpressStatus", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse updateOrderExpressStatus(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.updateOrderExpressStatus(request);
    }

    @ApiOperation(value = "评价小哥 评价顺丰订单",httpMethod = "POST")
    @RequestMapping(value = "/evaluateSingle", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse evaluate(@RequestBody Object object) throws Exception {
        return orderService.evaluateSingle(object);
    }

    @ApiOperation(value = "取消订单",httpMethod = "POST")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse cancelOrder(@RequestBody Object object) throws Exception {
        return orderService.cancelOrder(object);
    }

    @ApiOperation(value = "时间规则",httpMethod = "POST")
    @RequestMapping(value = "/constants", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse constants(HttpServletRequest request) throws Exception {
        return orderService.timeConstants(new APIRequest(request));
    }

    @ApiOperation(value = "30分钟未揽件同城单转大网单(给顺丰调用)",httpMethod = "POST")
    @RequestMapping(value = "/transform", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse transformOrder(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.transformOrderFromSameToNation(request);
    }

    @ApiOperation(value = "获取大网订购的评价信息",httpMethod = "GET")
    @RequestMapping(value = "/getEvaluate", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getEvaluate(String uuid) throws Exception {
        return evaluateService.getEvaluate(uuid);
    }

    @ApiOperation(value = "设置大网预约定时器",httpMethod = "POST")
    @RequestMapping(value = "/reserve/setup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupReserveTimer(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.setupReserveNationOrderCommitTimer(request);
    }

    @ApiOperation(value = "设置大网超时取消定时器",httpMethod = "POST")
    @RequestMapping(value = "/cancel/setup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupCancelNationOrderTimer(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.setupCancelNationOrderTimer(request);
    }

    @ApiOperation(value = "设置同城超时取消定时器",httpMethod = "POST")
    @RequestMapping(value = "/cancel/SameSetup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupCancelSameOrderTimer(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.setupCancelSameOrderTimer(request);
    }

    @ApiOperation(value = "设置兜底记录已读",httpMethod = "POST")
    @RequestMapping(value = "/transform/read", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse readExpressTransform(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.readExpressTransform(request);
    }

    @ApiOperation(value = "订单分享图片",httpMethod = "POST")
    @RequestMapping(value = "/share/screenShot", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse shareOrderImage(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return orderService.screenShot(request);
    }

//    @ApiOperation(value = "面对面取件",httpMethod = "POST")
//    @RequestMapping(value = "/faceOrder",method = RequestMethod.POST)
//    public @ResponseBody
//    APIResponse faceOrdered(@RequestBody Object object) throws Exception{
//        APIRequest apiRequest = new APIRequest();
//        apiRequest.setRequestParam(object);
//        return orderService.faceOrdered(apiRequest);
//    }
}
