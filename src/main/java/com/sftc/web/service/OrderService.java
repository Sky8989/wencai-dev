package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

public interface OrderService {

    /**
     * 普通订单提交
     */
    APIResponse addNormalOrderCommit(APIRequest request);

    /**
     * 好友订单提交
     */
    APIResponse addFriendOrderCommit(APIRequest request);

    /**
     * 支付订单
     */
    APIResponse payOrder(APIRequest request);

    /**
     * 寄件人填写订单
     */
    APIResponse friendPlaceOrder(APIRequest request);

    /**
     * 好友填写寄件订单
     */
    APIResponse friendFillOrder(APIRequest request);

    /**
     * 计价
     */
    APIResponse countPrice(APIRequest request);

    /**
     * 获取订单详情
     */
    APIResponse selectOrderDetail(APIRequest request);

    /**
     * 更改订单状态
     */
    APIResponse updateOrderStatus(APIRequest request);

    /**
     * 更改订单快递状态
     */
    APIResponse updateOrderExpressStatus(APIRequest request);

    /**
     * 我的订单列表
     */
    APIResponse getMyOrderList(APIRequest request);

    /**
     * 好友圈
     */
    APIResponse getMyFriendCircleOrderList(APIRequest request);

    /**
     * 快递详情
     */
    APIResponse selectExpressDetail(APIRequest request);

    /**
     * 纯走B端的同城快递详情查询
     */
    APIResponse selectSameExpressDetail(APIRequest request);

    /**
     * 评价某个订单的单一包裹
     */
    APIResponse evaluateSingle(APIRequest request);

    /**
     * 取消订单
     */
    APIResponse cancelOrder(APIRequest request);

    /**
     * 根据uuid取消订单
     */
    APIResponse cancelOrderByUuid(APIRequest request);

    /**
     * 兜底
     * 小哥30分钟未揽件，自动取消订单
     */
    APIResponse transformOrderFromSameToNation(APIRequest request);

    /**
     * 设置兜底记录已读
     */
    APIResponse readExpressTransform(APIRequest request);

    /**
     * 时间规则
     */
    APIResponse timeConstants(APIRequest request);

    /**
     * 设置同城取消超时订单定时器开关
     */
    APIResponse setupCancelSameOrderTimer(APIRequest request);

    /**
     * 订单分享界面截图
     */
    APIResponse screenShot(APIRequest request);

    /**
     * 根据地址经纬度判断是否可同城下单
     */
    APIResponse determineOrderAddress(APIRequest request);
}

