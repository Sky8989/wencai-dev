package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.OrderService;
import com.sftc.web.service.impl.logic.app.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderCreateLogic orderCreateLogic;
    @Resource
    private OrderCommitLogic orderCommitLogic;
    @Resource
    private OrderTimerLogic orderTimerLogic;
    @Resource
    private OrderCancelLogic orderCancelLogic;
    @Resource
    private OrderDetailLogic orderDetailLogic;
    @Resource
    private OrderListLogic orderListLogic;
    @Resource
    private OrderPayLogic orderPayLogic;
    @Resource
    private OrderEvaluateLogic orderEvaluateLogic;
    @Resource
    private OrderStatusLogic orderStatusLogic;
    @Resource
    private OrderOtherLogic orderOtherLogic;
    @Resource
    private OrderTransformLogic orderTransformLogic;

    public APIResponse addNormalOrderCommit(APIRequest request) {
        return orderCommitLogic.normalOrderCommit(request);
    }

    public APIResponse addFriendOrderCommit(APIRequest request) {
        return orderCommitLogic.friendOrderCommit(request);
    }

    public APIResponse setupCancelSameOrderTimer(APIRequest request) {
        return orderTimerLogic.setupCancelSameOrderTimer(request);
    }
    public APIResponse countPrice(APIRequest request) {
        return orderPayLogic.countPrice(request);
    }

    public APIResponse payOrder(APIRequest request) {
        return orderPayLogic.payOrder(request);
    }

    public APIResponse friendPlaceOrder(APIRequest request) {
        return orderCreateLogic.friendPlaceOrder(request);
    }

    public synchronized APIResponse friendFillOrder(APIRequest request) {
        return orderCreateLogic.friendFillOrder(request);
    }
    public APIResponse selectOrderDetail(APIRequest request) {
        return orderDetailLogic.selectOrderDetail(request);
    }

    public APIResponse selectExpressDetail(APIRequest request) {
        return orderDetailLogic.selectExpressDetail(request);
    }

    public APIResponse selectSameExpressDetail(APIRequest request) {
        return orderDetailLogic.selectSameExpressDetail(request);
    }

    public APIResponse updateOrderStatus(APIRequest request) {
        return orderStatusLogic.updateOrderStatus(request);
    }

    public APIResponse updateOrderExpressStatus(APIRequest request) {
        return orderStatusLogic.updateOrderExpressStatus(request);
    }

    public APIResponse getMyOrderList(APIRequest request) {
        return orderListLogic.getMyOrderList(request);
    }

    public APIResponse getMyFriendCircleOrderList(APIRequest request) {
        return orderListLogic.getMyFriendCircleOrderList(request);
    }

    public APIResponse screenShot(APIRequest request) {
        return orderOtherLogic.screenShot(request);
    }

    //面对面下单
//    public APIResponse faceOrdered(APIRequest request) {return orderCommitLogic.faceOrdered(request);}

    public APIResponse evaluateSingle(APIRequest request) {
        return orderEvaluateLogic.evaluateSingle(request);
    }

    public APIResponse cancelOrder(APIRequest request) {
        return orderCancelLogic.cancelOrder(request);
    }

    //根据uuid取消订单
    public APIResponse cancelOrderByUuid(APIRequest request) {
        return orderCancelLogic.cancelOrderByUuid(request);
    }

    public APIResponse transformOrderFromSameToNation(APIRequest request) {
        return orderTransformLogic.transformOrderFromSameToNation(request);
    }

    public APIResponse readExpressTransform(APIRequest request) {
        return orderTransformLogic.readExpressTransform(request);
    }

    public APIResponse timeConstants(APIRequest request) {
        return orderOtherLogic.timeConstants(request);
    }

    public APIResponse determineOrderAddress(APIRequest request) {
        return orderOtherLogic.determineOrderAddress(request);
    }


}
