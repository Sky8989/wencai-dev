package com.sftc.web.service.impl.logic.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.OrderExpressMapper;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Component
public class OrderStatusLogic {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;

    //////////////////// Public Method ////////////////////

    /**
     * 更改订单状态
     */
    public APIResponse updateOrderStatus(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        // Param
        int order_id = requestObject.getInt("order_id");
        String status = requestObject.getString("status");

        if (order_id < 1)
            return APIUtil.paramErrorResponse("参数order_id不能为空");
        String statusError = verifyParamStatus(request);
        if (statusError != null)
            return APIUtil.paramErrorResponse(statusError);

        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        if (order == null)
            return APIUtil.submitErrorResponse("订单不存在", null);

        // update
        for (OrderExpress oe : order.getOrderExpressList()) {
            orderExpressMapper.updateOrderExpressStatus(oe.getId(), status);
        }
        order = orderMapper.selectOrderDetailByOrderId(order_id);
        return APIUtil.getResponse(SUCCESS, order);
    }

    /**
     * 更改订单快递状态
     */
    public APIResponse updateOrderExpressStatus(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        // Param
        String uuid = requestObject.getString("uuid");
        String status = requestObject.getString("status");

        if (uuid == null || uuid.equals(""))
            return APIUtil.paramErrorResponse("参数uuid不能为空");
        String statusError = verifyParamStatus(request);
        if (statusError != null)
            return APIUtil.paramErrorResponse(statusError);

        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid + "");
        if (orderExpress == null)
            return APIUtil.submitErrorResponse("订单快递不存在", null);

        // update
        orderExpressMapper.updateOrderExpressStatus(orderExpress.getId(), status);
        orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        return APIUtil.getResponse(SUCCESS, orderExpress);
    }

    //////////////////// Private Method ////////////////////

    // 验证参数
    private String verifyParamStatus(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        String status = requestObject.getString("status");
        if (status == null || status.equals(""))
            return "参数status不能为空";
        if (!(status.equals("WAIT_FILL") ||
                status.equals("ALREADY_FILL") ||
                status.equals("INIT") ||
                status.equals("PAYING") ||
                status.equals("WAIT_HAND_OVER") ||
                status.equals("DELIVERING") ||
                status.equals("FINISHED") ||
                status.equals("ABNORMAL") ||
                status.equals("CANCELED") ||
                status.equals("WAIT_REFUND") ||
                status.equals("REFUNDING") ||
                status.equals("REFUNDED")))
            return "参数status不正确";

        return null;
    }

}
