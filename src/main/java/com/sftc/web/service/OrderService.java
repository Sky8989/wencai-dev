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

    //面对面取件
//    APIResponse faceOrdered(APIRequest request);


    //////////////// CMS ////////////////

    /**
     * 获取 订单列表 支持分页查询 和 条件查询
     */
    APIResponse selectOrderListByPage(APIRequest request);

    /**
     * 查询已取消的订单
     *
     * @param request 通用请求参数
     * @return 返回APIResponse
     */
     APIResponse selectCanceledOrderList(APIRequest request);

    /**
     * 根据地址经纬度判断是否可同城下单
     */
    APIResponse determineOrderAddress(APIRequest request);
}

