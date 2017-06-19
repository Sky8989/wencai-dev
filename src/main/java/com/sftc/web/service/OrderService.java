package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;

import com.sftc.web.model.Token;

import com.sftc.web.model.reqeustParam.MyOrderParam;

import com.sftc.web.model.sfmodel.Requests;

import javax.servlet.http.HttpServletRequest;


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
     * @param @request
     * @return
     */

    APIResponse placeOrder(Object object);
    /**
     * 支付订单
     * @param request
     * @return
     */
    APIResponse payOrder(APIRequest request);

    /**
     * 寄件人填写订单
     * @param request
     * @return
     */
    APIResponse friendPlaceOrder(APIRequest request);

    /**
     * 好友填写寄件订单
     * @param
     * @return
     */
    APIResponse friendFillOrder(OrderExpress orderExpress);


    /**
     * 计价
     * @param @request
     * @return
     */
    APIResponse countPrice(Object object);

   /**
    * 我的订单列表
    * @param request
    * @return
    * */
    APIResponse getAllOrder(APIRequest request);

    /**
    * @获取订单详情
    *@param
    * @return
    * */
    APIResponse getOrderDetile(Order order,OrderExpress orderExpress,Token token,String sort);
    APIResponse updateOrder(APIRequest request,Order order,OrderExpress orderExpress);


    /**
     * 返回未被填写的包裹
     * @param request
     * @return
     */
    APIResponse getEmptyPackage(APIRequest request);

    APIResponse getMyOrderList(MyOrderParam myOrderParam);
    // APIResponse getMyOrderList(APIRequest request);

    APIResponse friendPlace(Object object);

    public APIResponse sfOrderDetail(int order_id,String access_token,String uuid);

    APIResponse placeOrderDetail(String uuid,String access_token);
    APIResponse noPlaceOrderDetail(int order_id);
    /**
     * 评价小哥
     * @param
     * @return
     */
    APIResponse evaluate(Object object);
    /**
     * 取消订单
     * @param
     * @return
     */
    APIResponse deleteOrder(Object object);
    /**
     * 时间规则
     * @param
     * @return
     */
    APIResponse timeConstants(APIRequest request);
    /**
     * 大网下单接口
     * @param
     * @return
     */
    APIResponse createOrder(Object object);

    /**
     * 大网计价接口
     * @param
     * @return
     */
    APIResponse OrderFreightQuery(Object object);
    /**
     * 大网路由接口
     * @param
     * @return
     */
    APIResponse OrderRouteQuery(APIRequest request);
    /**
     * 大网好友下单
     * @param
     * @return
     */
    APIResponse globalFriendPlace(Object object);
    /**
     * 提醒寄件人下单
     * @param
     * @return
     */
    APIResponse remindPlace(APIRequest request);
}

