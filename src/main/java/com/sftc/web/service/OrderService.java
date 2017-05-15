package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import org.springframework.stereotype.Service;


/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service
 * @Description: 订单操作接口
 * @date 17/4/1
 * @Time 下午9:32
 */

public interface OrderService {

    /**
     * 普通提交订单
     * @param request
     * @return
     */
    APIResponse placeOrder(APIRequest request);

    /**
     * 支付订单
     * @param request
     * @return
     */
    APIResponse payOrder(APIRequest request);

    /**
     * 好友寄件提交订单
     * @param request
     * @return
     */
    APIResponse friendPlaceOrder(APIRequest request);

    /**
     * 好友填写寄件订单
     * @param request
     * @return
     */
    APIResponse friendFillOrder(APIRequest request);

   /*
    * 我的订单列表
    * @param request
    * @return
    * */
    APIResponse getAllOrder(APIRequest request);
    /*
    * @获取订单详情
    *@param request
    * @return
    * */
    APIResponse getOrderDetile(APIRequest request);
    APIResponse updateOrder(APIRequest request,Order order,OrderExpress orderExpress);
 }
