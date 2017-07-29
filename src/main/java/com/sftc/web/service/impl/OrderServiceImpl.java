package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.OrderService;
import com.sftc.web.service.impl.logic.api.*;
import com.sftc.web.service.impl.logic.cms.CMSOrderListLogic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

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
    private OrderTransformLogic orderTransformLogic;
    @Resource
    private OrderPayLogic orderPayLogic;
    @Resource
    private OrderEvaluateLogic orderEvaluateLogic;
    @Resource
    private OrderStatusLogic orderStatusLogic;
    @Resource
    private OrderOtherLogic orderOtherLogic;

    @Resource
    private CMSOrderListLogic cmsOrderListLogic;

    public APIResponse addNormalOrderCommit(APIRequest request) {
        return orderCommitLogic.normalOrderCommit(request);
    }

    public APIResponse addFriendOrderCommit(APIRequest request) {
        return orderCommitLogic.friendOrderCommit(request);
    }

    public APIResponse setupReserveNationOrderCommitTimer(APIRequest request) {
        return orderTimerLogic.setupReserveNationOrderCommitTimer(request);
    }

    public APIResponse setupCancelNationOrderTimer(APIRequest request) {
        return orderTimerLogic.setupCancelNationOrderTimer(request);
    }

    public APIResponse setupCancelSameOrderTimer(APIRequest request) {
        return orderTimerLogic.setupCancelSameOrderTimer(request);
    }

    public APIResponse transformOrderFromSameToNation(APIRequest request) {
        return orderTransformLogic.transformOrderFromSameToNation(request);
    }

    public APIResponse readExpressTransform(APIRequest request) {
        return orderTransformLogic.readExpressTransform(request);
    }

    public APIResponse countPrice(Object object) {
        return orderPayLogic.countPrice(object);
    }

    public APIResponse payOrder(APIRequest request) {
        return orderPayLogic.payOrder(request);
    }

    public APIResponse friendPlaceOrder(APIRequest request) {
        return orderCreateLogic.friendPlaceOrder(request);
    }

    public synchronized APIResponse friendFillOrder(Map rowData) {
        return orderCreateLogic.friendFillOrder(rowData);
    }

    public APIResponse selectOrderDetail(APIRequest request) {
        return orderDetailLogic.selectOrderDetail(request);
    }

    public APIResponse selectExpressDetail(APIRequest request) {
        return orderDetailLogic.selectExpressDetail(request);
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

    public APIResponse evaluateSingle(Object object) {
        return orderEvaluateLogic.evaluateSingle(object);
    }

    public APIResponse cancelOrder(Object object) {
        return orderCancelLogic.cancelOrder(object);
    }

    public APIResponse timeConstants(APIRequest request) {
        return orderOtherLogic.timeConstants(request);
    }

    //////////////// CMS ////////////////

    public APIResponse selectOrderListByPage(APIRequest request) {
        return cmsOrderListLogic.selectOrderListByPage(request);
    }
}
