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

    /**
     * 更改订单状态
     */
    APIResponse updateOrderStatus(APIRequest request);

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
     * 评价某个订单的单一包裹
     */
    APIResponse evaluateSingle(Object object);

    /**
     * 取消订单
     */
    APIResponse cancelOrder(Object object);

    /**
     * 时间规则
     */
    APIResponse timeConstants(APIRequest request);

    /**
     * 兜底
     * 根据同城订单的uuid，把原本同城的单下到大网
     */
    APIResponse transformOrderFromSameToNation(APIRequest request);

    /**
     * 设置大网预约单定时器开关
     */
    APIResponse setupReserveNationOrderCommitTimer(APIRequest request);

    /**
     * 设置大网取消超时订单定时器开关
     */
    APIResponse setupCancelNationOrderTimer(APIRequest request);

    /**
     * 设置大网取消超时订单定时器开关
     */
    APIResponse setupCancelSameOrderTimer(APIRequest request);

    /**
     * 设置兜底记录已读
     */
    APIResponse readExpressTransform(APIRequest request);

    /**
     * 订单分享界面截图
     */
    APIResponse screenShot(APIRequest request);


    //////////////// CMS ////////////////

    /**
     * 获取 订单列表 支持分页查询 和 条件查询
     */
    APIResponse selectOrderListByPage(APIRequest request);
}

