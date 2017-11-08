package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.SwaggerOrderVO.*;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.service.EvaluateService;
import com.sftc.web.service.OrderService;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Api(description = "订单相关接口")
@RequestMapping("order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;
    @Resource
    private EvaluateService evaluateService;

    @ApiOperation(value = "普通同城订单提交", httpMethod = "POST")
    @RequestMapping(value = "/normal/same", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse placeOrder(@RequestBody OrderRequestVO orderRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderRequestVO);
        return orderService.addNormalOrderCommit(request);
    }

    @ApiOperation(value = "普通大网订单提交", httpMethod = "POST")
    @RequestMapping(value = "/normal/nation", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse nationOrder(@RequestBody OrderNationRequestVO orderNationRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderNationRequestVO);
        return orderService.addNormalOrderCommit(request);
    }

    @ApiOperation(value = "好友同城订单提交", httpMethod = "POST")
    @RequestMapping(value = "/friend/same", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendOrderCommit(@RequestBody FriendOrderRequestVO friendOrderRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(friendOrderRequestVO);
        return orderService.addFriendOrderCommit(request);
    }

    @ApiOperation(value = "好友大网订单提交", httpMethod = "POST")
    @RequestMapping(value = "/friend/nation", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendNationOrderCommit(@RequestBody FriendNationOrderRequestVO friendNationOrderRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(friendNationOrderRequestVO);
        return orderService.addFriendOrderCommit(request);
    }

    @IgnoreToken
    @ApiOperation(value = "计价接口", httpMethod = "POST")
    @RequestMapping(value = "/valuation", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse countPrice(@RequestBody PriceRequestVO priceRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(priceRequestVO);
        return orderService.countPrice(request);
    }

    @IgnoreToken
    @ApiOperation(value = "好友填写收件", httpMethod = "POST")
    @RequestMapping(value = "friend/fill", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse fillOrder(@RequestBody FriendFillVO friendFillVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(friendFillVO);
        return orderService.friendFillOrder(request);
    }

    @ApiOperation(value = "寄件人填写订单", httpMethod = "POST")
    @RequestMapping(value = "sender/fill", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendPlaceOrder(@RequestBody OrderParam orderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderParam);
        return orderService.friendPlaceOrder(request);
    }

    @ApiOperation(value = "支付订单", httpMethod = "POST")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse payOrder(@RequestBody OrderPayVO orderPayVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderPayVO);
        return orderService.payOrder(request);
    }

    @ApiOperation(value = "订单详情", httpMethod = "GET")
    @ApiImplicitParam(name = "order_id", value = "订单id", defaultValue = "C1508130348092UT", paramType = "query", required = true)
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return orderService.selectOrderDetail(new APIRequest(request));
    }

    @ApiOperation(value = "我的订单列表", httpMethod = "POST")
    @RequestMapping(value = "/my", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse myOrder(@RequestBody MyOrderParam myOrderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(myOrderParam);
        return orderService.getMyOrderList(request);
    }

    @ApiOperation(value = "我的好友圈订单列表", httpMethod = "POST")
    @RequestMapping(value = "/list/friend", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendOrder(@RequestBody MyOrderParam myOrderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(myOrderParam);
        return orderService.getMyFriendCircleOrderList(request);
    }

    @IgnoreToken
    @ApiOperation(value = "快递详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "快递uuid", defaultValue = "e1d2ceda3daa", paramType = "query", required = true),
            @ApiImplicitParam(name = "access_token", value = "顺丰token", defaultValue = "EyMivbd44I124lcddrBG", paramType = "query", required = true),
            @ApiImplicitParam(name = "sort", value = "排序方式", defaultValue = "asc", paramType = "query")
    })
    @RequestMapping(value = "/express/detail", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse expressDetail(HttpServletRequest request) throws Exception {
        return orderService.selectExpressDetail(new APIRequest(request));
    }

    @IgnoreToken
    @ApiOperation(value = "B+C端同城专送快递详情", httpMethod = "GET")
    @ApiImplicitParam(name = "uuid",value = "uuid",defaultValue = "2c9a85895f721c3e015f782b519d23db",paramType = "query",required = true)
    @RequestMapping(value = "/express/query", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse sameExpressDetail(HttpServletRequest request) throws Exception {
        return orderService.selectSameExpressDetail(new APIRequest(request));
    }

    @ApiOperation(value = "更改订单状态", httpMethod = "PATCH")
    @RequestMapping(value = "/status", method = RequestMethod.PATCH)
    public @ResponseBody
    APIResponse updateOrderStatus(@RequestBody OrderStatusVO rderStatusVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(rderStatusVO);
        return orderService.updateOrderStatus(request);
    }

    @ApiOperation(value = "更改快递状态", httpMethod = "PATCH")
    @RequestMapping(value = "/express/status", method = RequestMethod.PATCH)
    public @ResponseBody
    APIResponse updateOrderExpressStatus(@RequestBody OrderExpressStatusVO orderExpressStatusVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderExpressStatusVO);
        return orderService.updateOrderExpressStatus(request);
    }

    @ApiOperation(value = "评价小哥 评价顺丰订单", httpMethod = "POST")
    @RequestMapping(value = "/evaluate", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse evaluate(@RequestBody EvaluateRequestVO evaluateRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(evaluateRequestVO);
        return orderService.evaluateSingle(request);
    }

    @ApiOperation(value = "取消订单", httpMethod = "POST")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse cancelOrder(@RequestBody OrderCancelVO orderCancelVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderCancelVO);
        return orderService.cancelOrder(request);
    }

    @ApiOperation(value = "时间规则", httpMethod = "POST")
    @RequestMapping(value = "/constants", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse constants(@RequestBody OrderContantsVO orderContantsVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderContantsVO);
        return orderService.timeConstants(request);
    }

    @IgnoreToken
    @ApiOperation(value = "30分钟未揽件同城单转大网单(给顺丰调用)", httpMethod = "POST")
    @RequestMapping(value = "/transform", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse transformOrder(@RequestBody OrderTransform orderTransform) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderTransform);
        return orderService.transformOrderFromSameToNation(request);
    }

    @ApiOperation(value = "获取大网订购的评价信息", httpMethod = "GET")
    @ApiImplicitParam(name = "uuid", value = "快递uuid", defaultValue = "2c9a85895d8f0962015d90246c8c0a4f", paramType = "query", required = true)
    @RequestMapping(value = "/evaluate", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getEvaluate(String uuid) throws Exception {
        return evaluateService.getEvaluate(uuid);
    }

    @ApiOperation(value = "设置大网预约定时器", httpMethod = "POST")
    @RequestMapping(value = "/reserve/setup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupReserveTimer(@RequestBody OrderTimeVO orderTimeVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderTimeVO);
        return orderService.setupReserveNationOrderCommitTimer(request);
    }

    @ApiOperation(value = "设置大网超时取消定时器", httpMethod = "POST")
    @RequestMapping(value = "/cancel/setup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupCancelNationOrderTimer(@RequestBody OrderCancelTimeVO orderCancelTimeVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderCancelTimeVO);
        return orderService.setupCancelNationOrderTimer(request);
    }

    @ApiOperation(value = "设置同城超时取消定时器", httpMethod = "POST")
    @RequestMapping(value = "/cancel/SameSetup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupCancelSameOrderTimer(@RequestBody OrderCancelTimeVO orderCancelTimeVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderCancelTimeVO);
        return orderService.setupCancelSameOrderTimer(request);
    }

    @IgnoreToken
    @ApiOperation(value = "设置兜底记录已读", httpMethod = "PATCH")
    @RequestMapping(value = "/transform", method = RequestMethod.PATCH)
    public @ResponseBody
    APIResponse readExpressTransform(@RequestBody OrderTransformIsReadVO orderTransformIsReadVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderTransformIsReadVO);
        return orderService.readExpressTransform(request);
    }

    @ApiOperation(value = "订单分享图片", httpMethod = "POST")
    @RequestMapping(value = "/share/screenShot", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse shareOrderImage(@RequestBody OrderPictureVO orderPictureVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderPictureVO);
        return orderService.screenShot(request);
    }

}
