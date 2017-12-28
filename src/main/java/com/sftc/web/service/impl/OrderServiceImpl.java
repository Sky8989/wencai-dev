package com.sftc.web.service.impl;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerOrderRequest.*;
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

    @Override
    public ApiResponse addNormalOrderCommit(OrderRequestVO orderRequestVO) {
        return orderCommitLogic.normalOrderCommit(orderRequestVO);
    }

    @Override
    public ApiResponse addFriendOrderCommit(FriendOrderRequestVO friendOrderRequestVO) {
        return orderCommitLogic.friendOrderCommit(friendOrderRequestVO);
    }

    @Override
    public ApiResponse countPrice(PriceRequestVO priceRequestVO) {
        return orderPayLogic.countPrice(priceRequestVO);
    }

    @Override
    public ApiResponse payOrder(OrderPayVO orderPayVO) {
        return orderPayLogic.payOrder(orderPayVO);
    }

    @Override
    public ApiResponse friendPlaceOrder(OrderParamVO orderParamVO) {
        return orderCreateLogic.friendPlaceOrder(orderParamVO);
    }

    @Override
    public synchronized ApiResponse friendFillOrder(FriendFillVO friendFillVO) {
        return orderCreateLogic.friendFillOrder(friendFillVO);
    }

    @Override
    public ApiResponse selectOrderDetail(String orderId) {
        return orderDetailLogic.selectOrderDetail(orderId);
    }

    @Override
    public ApiResponse selectExpressDetail(String uuid) {
        return orderDetailLogic.selectExpressDetail(uuid);
    }

    @Override
    public ApiResponse selectSameExpressDetail(String uuid) {
        return orderDetailLogic.selectSameExpressDetail(uuid);
    }

    @Override
    public ApiResponse updateOrderStatus(OrderStatusVO orderStatusVO) {
        return orderStatusLogic.updateOrderStatus(orderStatusVO);
    }

    @Override
    public ApiResponse updateOrderExpressStatus(OrderExpressStatusVO orderExpressStatusVO) {
        return orderStatusLogic.updateOrderExpressStatus(orderExpressStatusVO);
    }

    @Override
    public ApiResponse getMyOrderList(MyOrderParamVO myOrderParamVO) {
        return orderListLogic.getMyOrderList(myOrderParamVO);
    }

    @Override
    public ApiResponse getMyFriendCircleOrderList(MyOrderParamVO myOrderParamVO) {
        return orderListLogic.getMyFriendCircleOrderList(myOrderParamVO);
    }

    @Override
    public ApiResponse screenShot(OrderPictureVO orderPictureVO) {
        return orderOtherLogic.screenShot(orderPictureVO);
    }

    @Override
    public ApiResponse evaluateSingle(EvaluateRequestVO evaluateRequestVO) {
        return orderEvaluateLogic.evaluateSingle(evaluateRequestVO);
    }

    @Override
    public ApiResponse cancelOrder(OrderCancelVO orderCancelVO) {
        return orderCancelLogic.cancelOrder(orderCancelVO);
    }

    /**
     * 根据uuid取消订单
     */
    @Override
    public ApiResponse cancelOrderByUuid(ExpressCancelVO expressCancelVO) {
        return orderCancelLogic.cancelOrderByUuid(expressCancelVO);
    }

    @Override
    public ApiResponse transformOrderFromSameToNation(OrderTransform orderTransform) {
        return orderTransformLogic.transformOrderFromSameToNation(orderTransform);
    }

    @Override
    public ApiResponse readExpressTransform(OrderTransformIsReadVO orderTransformIsReadVO) {
        return orderTransformLogic.readExpressTransform(orderTransformIsReadVO);
    }

    @Override
    public ApiResponse timeConstants(OrderContantsVO orderContantsVO) {
        return orderOtherLogic.timeConstants(orderContantsVO);
    }

    @Override
    public ApiResponse determineOrderAddress(OrderAddressDetermineVO orderAddressDetermineVO) {
        return orderOtherLogic.determineOrderAddress(orderAddressDetermineVO);
    }


}
