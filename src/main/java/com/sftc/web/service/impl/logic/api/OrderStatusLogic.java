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

    /**
     * 更改订单状态
     */
    public APIResponse updateOrderStatus(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        // Param
        int order_id = ((Double) requestObject.get("order_id")).intValue();
        String status = (String) requestObject.get("status");
        if (order_id < 1)
            return APIUtil.paramErrorResponse("参数order_id不能为空");
        if (status == null || status.equals(""))
            return APIUtil.paramErrorResponse("参数status不能为空");
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
                status.equals("REFUNDED"))) {
            return APIUtil.paramErrorResponse("参数status不正确");
        }
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
}
