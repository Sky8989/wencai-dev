package com.sftc.web.service.impl.logic.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.OrderExpress;
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
    @Resource
    private OrderExpressDao orderExpressDao;

    //////////////////// Public Method ////////////////////

    /**
     * 更改订单状态
     */
    public APIResponse updateOrderStatus(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        // Param
        String order_id = requestObject.getString("order_id");
        String route_state = requestObject.getString("route_state");
        String pay_state = requestObject.getString("pay_state");

        if (order_id == null || order_id.equals(""))
            return APIUtil.paramErrorResponse("参数order_id不能为空");
        String statusError = verifyParamStatus(request);
        if (statusError != null)
            return APIUtil.paramErrorResponse(statusError);

        OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(order_id);
        if (orderDTO == null)
            return APIUtil.submitErrorResponse("订单不存在", null);

        // update
        for (OrderExpressDTO oe : orderDTO.getOrderExpressList()) {
            String uuid = oe.getUuid();
            //事务问题,先存在查的改为统一使用Mybatis
            orderExpressMapper.updateOrderExpressStatusByUUID(uuid, route_state, pay_state);
        }
        orderDTO = orderMapper.selectOrderDetailByOrderId(order_id);
        return APIUtil.getResponse(SUCCESS, orderDTO);
    }

    /**
     * 更改订单快递状态
     */
    public APIResponse updateOrderExpressStatus(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        // Param
        String uuid = requestObject.getString("uuid");
        String route_state = requestObject.getString("route_state");
        String pay_state = requestObject.getString("pay_state");

        if (uuid == null || uuid.equals(""))
            return APIUtil.paramErrorResponse("参数uuid不能为空");
        String statusError = verifyParamStatus(request);
        if (statusError != null)
            return APIUtil.paramErrorResponse(statusError);

        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid + "");
        if (orderExpress == null)
            return APIUtil.submitErrorResponse("订单快递不存在", null);

        // update
//        orderExpress.setState(status);
//        orderExpressDao.save(orderExpress);
        //事务问题,先存在查的改为统一使用Mybatis
        orderExpressMapper.updateOrderExpressStatusByUUID(uuid, route_state, pay_state);
        orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        return APIUtil.getResponse(SUCCESS, orderExpress);
    }

    //////////////////// Private Method ////////////////////

    // 验证参数
    private String verifyParamStatus(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        String route_state = requestObject.getString("route_state");
        String pay_state = requestObject.getString("pay_state");
        if (route_state == null || route_state.equals("")||pay_state == null || pay_state.equals(""))
            return "参数status不能为空";
        if (!(route_state.equals("WAIT_FILL") ||
                route_state.equals("ALREADY_FILL") ||
                route_state.equals("INIT") ||
                route_state.equals("PAYING") ||
                route_state.equals("WAIT_HAND_OVER") ||
                route_state.equals("DELIVERING") ||
                route_state.equals("FINISHED") ||
                route_state.equals("ABNORMAL") ||
                route_state.equals("CANCELED") ||
                route_state.equals("REFUNDING")||
                pay_state.equals("WAIT_PAY")||
                pay_state.equals("ALREADY_PAY")||
                pay_state.equals("WAIT_REFUND")||
                pay_state.equals("REFUNDED")))
            return "参数status不正确";

        return null;
    }

}
