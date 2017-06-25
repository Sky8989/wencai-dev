package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.Token;

import java.util.Map;


public interface OrderService {

    /**
     * 普通订单提交
     */
    APIResponse normalOrderCommit(APIRequest request);

    /**
     * 好友订单提交
     */
    APIResponse friendOrderCommit(APIRequest request);

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
    APIResponse friendFillOrder(Map rowData);

    /**
     * 计价
     */
    APIResponse countPrice(Object object);

    /**
     * 获取订单详情
     */
    APIResponse selectOrderDetail(APIRequest request);

    APIResponse updateOrder(APIRequest request, Order order, OrderExpress orderExpress);

    /**
     * 返回未被填写的包裹
     */
    APIResponse getEmptyPackage(APIRequest request);

    /**
     * 我的订单列表
     */
    APIResponse getMyOrderList(APIRequest request);

    /**
     * 好友圈
     */
    APIResponse getMyFriendCircleOrderList(APIRequest request);

    APIResponse friendPlace(Object object);

    public APIResponse sfOrderDetail(int order_id, String access_token, String uuid);

    /**
     * 快递详情
     */
    APIResponse selectExpressDetail(APIRequest request);

    APIResponse noPlaceOrderDetail(int order_id);

    /**
     * 评价小哥
     */
    APIResponse evaluate(Object object);

    /**
     * 取消订单
     */
    APIResponse deleteOrder(Object object);

    /**
     * 时间规则
     */
    APIResponse timeConstants(APIRequest request);

    /**
     * 大网计价接口
     */
    APIResponse OrderFreightQuery(Object object);

    /**
     * 大网路由接口
     */
    APIResponse OrderRouteQuery(APIRequest request);

    /**
     * 提醒寄件人下单
     */
    APIResponse remindPlace(APIRequest request);
}

