package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.Token;

import java.util.Map;


public interface OrderService {

    /**
     * 普通提交订单
     */
    APIResponse placeOrder(Object object);

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
    APIResponse getOrderDetile(Order order, OrderExpress orderExpress, Token token, String sort);

    APIResponse updateOrder(APIRequest request, Order order, OrderExpress orderExpress);

    /**
     * 返回未被填写的包裹
     */
    APIResponse getEmptyPackage(APIRequest request);

    APIResponse getMyOrderList(APIRequest request);

    APIResponse friendPlace(Object object);

    public APIResponse sfOrderDetail(int order_id, String access_token, String uuid);

    APIResponse placeOrderDetail(String uuid, String access_token);

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
     * 大网下单接口
     */
    APIResponse createOrder(Object object);

    /**
     * 大网计价接口
     */
    APIResponse OrderFreightQuery(Object object);

    /**
     * 大网路由接口
     */
    APIResponse OrderRouteQuery(APIRequest request);

    /**
     * 大网好友下单
     */
    APIResponse globalFriendPlace(Object object);

    /**
     * 提醒寄件人下单
     */
    APIResponse remindPlace(APIRequest request);
}

