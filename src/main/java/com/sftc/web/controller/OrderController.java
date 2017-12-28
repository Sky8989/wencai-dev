package com.sftc.web.controller;

import com.sftc.tools.api.ApiRequest;
import com.sftc.tools.api.ApiResponse;
import com.sftc.web.config.IgnoreToken;
import com.sftc.web.model.vo.swaggerOrderRequest.*;
import com.sftc.web.model.vo.swaggerResponse.*;
import com.sftc.web.service.EvaluateService;
import com.sftc.web.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Api(description = "订单相关接口")
@RequestMapping("order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;
    @Resource
    private EvaluateService evaluateService;

    @ApiOperation(value = "普通同城订单提交", httpMethod = "POST", notes = "request为同城订单需要的参数")
    @RequestMapping(value = "/normal/same", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse placeOrder(@RequestBody @Valid OrderRequestVO body) throws Exception {
        return orderService.addNormalOrderCommit(body);
    }

    @ApiOperation(value = "好友同城订单提交", httpMethod = "POST", notes = "request为同城订单需要的参数")
    @RequestMapping(value = "/friend/same", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse friendOrderCommit(@RequestBody @Valid FriendOrderRequestVO body) throws Exception {
        return orderService.addFriendOrderCommit(body);
    }

    @IgnoreToken
    @ApiOperation(value = "计价接口", httpMethod = "POST", notes = "access_token 和 uuid 要么都有，要么都没有")
    @RequestMapping(value = "/valuation", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse countPrice(@RequestBody @Valid PriceRequestVO body) throws Exception {
        return orderService.countPrice(body);
    }

    @IgnoreToken
    @ApiOperation(value = "好友填写收件", httpMethod = "POST")
    @RequestMapping(value = "friend/fill", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse fillOrder(@RequestBody @Valid FriendFillVO body) throws Exception {
        return orderService.friendFillOrder(body);
    }

    @ApiOperation(value = "寄件人填写订单", httpMethod = "POST")
    @RequestMapping(value = "sender/fill", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse friendPlaceOrder(@RequestBody @Valid OrderParamVO body) throws Exception {
        return orderService.friendPlaceOrder(body);
    }

    @ApiOperation(value = "支付订单", httpMethod = "POST")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse payOrder(@RequestBody @Valid OrderPayVO body) throws Exception {
        return orderService.payOrder(body);
    }

    @ApiOperation(value = "订单详情", httpMethod = "GET")
    @ApiImplicitParam(name = "order_id", value = "订单id", defaultValue = "C1512027718290FV", paramType = "query", required = true)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse detail(@RequestParam(value = "order_id") @Valid String orderId) throws Exception {
        return orderService.selectOrderDetail(orderId);
    }

    @ApiOperation(value = "我的订单列表", httpMethod = "POST", response = OrderListRespVO.class)
    @RequestMapping(value = "/my", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse myOrder(@RequestBody @Valid MyOrderParamVO body) throws Exception {
        return orderService.getMyOrderList(body);
    }

    @ApiOperation(value = "我的好友圈订单列表", httpMethod = "POST")
    @RequestMapping(value = "/list/friend", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse friendOrder(@RequestBody @Valid MyOrderParamVO body) throws Exception {
        return orderService.getMyFriendCircleOrderList(body);
    }

    @IgnoreToken
    @ApiOperation(value = "快递详情", httpMethod = "GET", notes = "uuid必填，同城需要传 access_token，\n" +
            "返回的【order】是一定会有的，同城的快递会返回【request】如果是兜底单，会返回【transform】，\n" +
            "至于页面展不展示提示，请拿【transform】下的 is_read 字段。", response = OrderDetailRespVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "快递uuid", defaultValue = "2c9a8589600bed7501600c3329580664", paramType = "query", required = true),
            @ApiImplicitParam(name = "sort", value = "排序方式", defaultValue = "asc", paramType = "query")
    })
    @RequestMapping(value = "/express/detail", method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse expressDetail(@RequestParam(value = "uuid") String uuid) throws Exception {
        return orderService.selectExpressDetail(uuid);
    }

    @IgnoreToken
    @ApiOperation(value = "B+C端同城专送快递详情", httpMethod = "GET", notes = "纯走B端的快递详情")
    @ApiImplicitParam(name = "uuid", value = "uuid", defaultValue = "2c9a85895f721c3e015f782b519d23db", paramType = "query", required = true)
    @RequestMapping(value = "/express/query", method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse sameExpressDetail(@RequestParam(value = "uuid") String uuid) throws Exception {
        return orderService.selectSameExpressDetail(uuid);
    }

    @ApiOperation(value = "更改订单状态", httpMethod = "PUT", notes = "该接口只适用于同城的订单，在支付完成后修改路由状态和支付状态。\n" +
            "status 详见文档说明中的订单常量，一般只需要用到 PAYING 和 WAIT_HAND_OVER 这两个参数。\n" +
            "调起支付前将状态改为 PAYING，支付完成后将状态改为 WAIT_HAND_OVER。\n" +
            "当然也可以只调用用一次，支付完成后直接将状态改为 WAIT_HAND_OVER。\n" +
            "更改状态成功后会返回订单详情。", response = OrderStateRespVO.class)
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public @ResponseBody
    ApiResponse updateOrderStatus(@RequestBody @Valid OrderStatusVO body) throws Exception {
        return orderService.updateOrderStatus(body);
    }

    @ApiOperation(value = "更改快递状态", httpMethod = "PUT", response = OrderExpressStateRespVO.class)
    @RequestMapping(value = "/express/status", method = RequestMethod.PUT)
    public @ResponseBody
    ApiResponse updateOrderExpressStatus(@RequestBody @Valid OrderExpressStatusVO body) throws Exception {
        return orderService.updateOrderExpressStatus(body);
    }

    @ApiOperation(value = "评价小哥 评价顺丰订单", httpMethod = "POST", notes = "这里是对某一个包裹进行单独评价", response = OrderEvaluateRespVO.class)
    @RequestMapping(value = "/evaluate", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse evaluate(@RequestBody @Valid EvaluateRequestVO body) throws Exception {
        return orderService.evaluateSingle(body);
    }

    @ApiOperation(value = "取消订单", httpMethod = "POST")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse cancelOrder(@RequestBody @Valid OrderCancelVO body) throws Exception {
        return orderService.cancelOrder(body);
    }

    @ApiOperation(value = "根据uuid取消订单", httpMethod = "POST")
    @RequestMapping(value = "/express/cancel", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse cancelOrder(@RequestBody @Valid ExpressCancelVO body) throws Exception {
        return orderService.cancelOrderByUuid(body);
    }

    @IgnoreToken
    @ApiOperation(value = "快递小哥30分钟未揽件自动取消(给顺丰调用)", httpMethod = "POST")
    @RequestMapping(value = "/transform", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse transformOrder(@RequestBody @Valid OrderTransform body) throws Exception {
        return orderService.transformOrderFromSameToNation(body);
    }

    @IgnoreToken
    @ApiOperation(value = "设置兜底记录已读", httpMethod = "PUT")
    @RequestMapping(value = "/transform", method = RequestMethod.PUT)
    public @ResponseBody
    ApiResponse readExpressTransform(@RequestBody @Valid OrderTransformIsReadVO body) throws Exception {
        return orderService.readExpressTransform(body);
    }

    @ApiOperation(value = "获取订单基础数据", httpMethod = "POST")
    @RequestMapping(value = "/constants", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse constants(@RequestBody @Valid OrderContantsVO body) throws Exception {
        return orderService.timeConstants(body);
    }

    @ApiOperation(value = "获取订单的评价信息", httpMethod = "GET")
    @ApiImplicitParam(name = "uuid", value = "快递uuid", defaultValue = "2c9a8589600bed7501600d207fcd12ba", paramType = "query", required = true)
    @RequestMapping(value = "/evaluate", method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse getEvaluate(HttpServletRequest httpServletRequest) throws Exception {
        return evaluateService.getEvaluate(new ApiRequest(httpServletRequest));
    }

    @ApiOperation(value = "订单分享图片", httpMethod = "POST")
    @RequestMapping(value = "/share/screenShot", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse shareOrderImage(@RequestBody @Valid OrderPictureVO body) throws Exception {
        return orderService.screenShot(body);
    }

    @ApiOperation(value = "判断是否可同城下单", httpMethod = "POST")
    @RequestMapping(value = "/address/determine", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse shareOrderImage(@RequestBody @Valid OrderAddressDetermineVO body) throws Exception {
        return orderService.determineOrderAddress(body);
    }

}
