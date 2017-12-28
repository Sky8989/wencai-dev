package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerOrderRequest.*;

public interface OrderService {

    /**
     * 普通订单提交
     */
    ApiResponse addNormalOrderCommit(OrderRequestVO orderRequestVO);

    /**
     * 好友订单提交
     */
    ApiResponse addFriendOrderCommit(FriendOrderRequestVO friendOrderRequestVO);

    /**
     * 支付订单
     */
    ApiResponse payOrder(OrderPayVO orderPayVO);

    /**
     * 寄件人填写订单
     */
    ApiResponse friendPlaceOrder(OrderParamVO orderParamVO);

    /**
     * 好友填写寄件订单
     */
    ApiResponse friendFillOrder(FriendFillVO friendFillVO);

    /**
     * 计价
     */
    ApiResponse countPrice(PriceRequestVO priceRequestVO);

    /**
     * 获取订单详情
     * @param orderId 订单编号
     * @return 同步后的订单详情
     */
    ApiResponse selectOrderDetail(String orderId);

    /**
     * 更改订单状态
     */
    ApiResponse updateOrderStatus(OrderStatusVO orderStatusVO);

    /**
     * 更改订单快递状态
     */
    ApiResponse updateOrderExpressStatus(OrderExpressStatusVO orderExpressStatusVO);

    /**
     * 我的订单列表
     */
    ApiResponse getMyOrderList(MyOrderParamVO myOrderParamVO);

    /**
     * 好友圈
     */
    ApiResponse getMyFriendCircleOrderList(MyOrderParamVO myOrderParamVO);

    /**
     * 快递详情
     */
    ApiResponse selectExpressDetail(String uuid);

    /**
     * 纯走B端的同城快递详情查询
     */
    ApiResponse selectSameExpressDetail(String uuid);

    /**
     * 评价某个订单的单一包裹
     */
    ApiResponse evaluateSingle(EvaluateRequestVO evaluateRequestVO);

    /**
     * 取消订单
     */
    ApiResponse cancelOrder(OrderCancelVO orderCancelVO);

    /**
     * 根据uuid取消订单
     */
    ApiResponse cancelOrderByUuid(ExpressCancelVO expressCancelVO);

    /**
     * 兜底
     * 小哥30分钟未揽件，自动取消订单
     */
    ApiResponse transformOrderFromSameToNation(OrderTransform orderTransform);

    /**
     * 设置兜底记录已读
     */
    ApiResponse readExpressTransform(OrderTransformIsReadVO orderTransformIsReadVO);

    /**
     * 获取订单基础数据
     */
    ApiResponse timeConstants(OrderContantsVO orderContantsVO);

    /**
     * 订单分享界面截图
     */
    ApiResponse screenShot(OrderPictureVO orderPictureVO);

    /**
     * 根据地址经纬度判断是否可同城下单
     */
    ApiResponse determineOrderAddress(OrderAddressDetermineVO orderAddressDetermineVO);
}

