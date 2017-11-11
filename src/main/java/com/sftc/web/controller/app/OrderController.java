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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

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

    @ApiOperation(value = "普通同城订单提交", httpMethod = "POST", notes = "request为同城订单需要的参数")
    @RequestMapping(value = "/normal/same", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse placeOrder(@RequestBody OrderRequestVO orderRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderRequestVO);
        return orderService.addNormalOrderCommit(request);
    }

    @ApiOperation(value = "好友同城订单提交", httpMethod = "POST", notes = "request为同城订单需要的参数")
    @RequestMapping(value = "/friend/same", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendOrderCommit(@RequestBody FriendOrderRequestVO friendOrderRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(friendOrderRequestVO);
        return orderService.addFriendOrderCommit(request);
    }

    @IgnoreToken
    @ApiOperation(value = "计价接口", httpMethod = "POST", notes = "access_token 和 uuid 要么都有，要么都没有")
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

    @ApiOperation(value = "寄件人填写订单", httpMethod = "POST",notes = "请将请求体中region_type的默认参数string去掉")
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

    @ApiOperation(value = "我的订单列表", httpMethod = "POST",notes = "测试接口若出现错误，删除key_words、key_state再试试")
    @RequestMapping(value = "/my", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse myOrder(@RequestBody MyOrderParam myOrderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(myOrderParam);
        return orderService.getMyOrderList(request);
    }

    @ApiOperation(value = "我的好友圈订单列表", httpMethod = "POST",notes = "测试接口若出现错误，删除key_words、key_state再试试")
    @RequestMapping(value = "/list/friend", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse friendOrder(@RequestBody MyOrderParam myOrderParam) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(myOrderParam);
        return orderService.getMyFriendCircleOrderList(request);
    }

    @IgnoreToken
    @ApiOperation(value = "快递详情", httpMethod = "GET", notes = "uuid必填，同城需要传 access_token，\n" +
            "返回的【order】是一定会有的，同城的快递会返回【request】如果是兜底单，会返回【transform】，\n" +
            "至于页面展不展示提示，请拿【transform】下的 is_read 字段。")
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
    @ApiOperation(value = "B+C端同城专送快递详情", httpMethod = "GET",notes = "纯走B端的快递详情")
    @ApiImplicitParam(name = "uuid",value = "uuid",defaultValue = "2c9a85895f721c3e015f782b519d23db",paramType = "query",required = true)
    @RequestMapping(value = "/express/query", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse sameExpressDetail(HttpServletRequest request) throws Exception {
        return orderService.selectSameExpressDetail(new APIRequest(request));
    }

    @ApiOperation(value = "更改订单状态", httpMethod = "PUT", notes = "该接口只适用于同城的订单，在支付完成后将订单状态改为待揽件。\n" +
            "status 详见文档说明中的订单常量，一般只需要用到 PAYING 和 WAIT_HAND_OVER 这两个参数。\n" +
            "调起支付前将状态改为 PAYING，支付完成后将状态改为 WAIT_HAND_OVER。\n" +
            "当然也可以只调用用一次，支付完成后直接将状态改为 WAIT_HAND_OVER。\n" +
            "更改状态成功后会返回订单详情。")
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse updateOrderStatus(@RequestBody OrderStatusVO rderStatusVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(rderStatusVO);
        return orderService.updateOrderStatus(request);
    }

    @ApiOperation(value = "更改快递状态", httpMethod = "PUT")
    @RequestMapping(value = "/express/status", method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse updateOrderExpressStatus(@RequestBody OrderExpressStatusVO orderExpressStatusVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderExpressStatusVO);
        return orderService.updateOrderExpressStatus(request);
    }

    @ApiOperation(value = "评价小哥 评价顺丰订单", httpMethod = "POST",notes = "这里是对某一个包裹进行单独评价")
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

    @ApiOperation(value = "获取订单基础数据", httpMethod = "POST")
    @RequestMapping(value = "/constants", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse constants(@RequestBody OrderContantsVO orderContantsVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderContantsVO);
        return orderService.timeConstants(request);
    }

    @ApiOperation(value = "获取订单评价信息", httpMethod = "GET")
    @ApiImplicitParam(name = "uuid", value = "快递uuid", defaultValue = "2c9a85895d8f0962015d90246c8c0a4f", paramType = "query", required = true)
    @RequestMapping(value = "/evaluate", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getEvaluate(HttpServletRequest httpServletRequest) throws Exception {
        return evaluateService.getEvaluate(new APIRequest(httpServletRequest));
    }

    @ApiOperation(value = "设置同城超时取消定时器", httpMethod = "POST",notes = "默认自动开启该定时器,开/关 1/0")
    @RequestMapping(value = "/cancel/SameSetup", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setupCancelSameOrderTimer(@RequestBody OrderCancelTimeVO orderCancelTimeVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(orderCancelTimeVO);
        return orderService.setupCancelSameOrderTimer(request);
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
