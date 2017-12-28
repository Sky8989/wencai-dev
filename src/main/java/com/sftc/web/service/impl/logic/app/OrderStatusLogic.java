package com.sftc.web.service.impl.logic.app;


import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderExpressStatusVO;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderStatusVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.sftc.tools.api.ApiStatus.SUCCESS;

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
    public ApiResponse updateOrderStatus(OrderStatusVO orderStatusVO) {
        // Param
        String orderId = orderStatusVO.getOrder_id();
        String routeState = orderStatusVO.getRoute_state();
        String payState = orderStatusVO.getPay_state();

        String statusError = verifyParamStatus(routeState, payState);
        if (statusError != null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), statusError);
        }

        OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(orderId);
        if (orderDTO == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }

        for (OrderExpressDTO oe : orderDTO.getOrderExpressList()) {
            if ("ALREADY_FILL".equals(oe.getRoute_state())) {
                String uuid = oe.getUuid();
                //事务问题,先存在查的改为统一使用Mybatis
                orderExpressMapper.updateOrderExpressStatusByUUID(uuid, routeState, payState);
            }
        }
        orderDTO = orderMapper.selectOrderDetailByOrderId(orderId);
        return ApiUtil.getResponse(SUCCESS, orderDTO);
    }

    /**
     * 更改订单快递状态
     */
    public ApiResponse updateOrderExpressStatus(OrderExpressStatusVO orderExpressStatusVO) {

        String uuid = orderExpressStatusVO.getUuid();
        String routeState = orderExpressStatusVO.getRoute_state();
        String payState = orderExpressStatusVO.getPay_state();

        if (StringUtils.isBlank(uuid)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "参数uuid不能为空");
        }
        String statusError = verifyParamStatus(routeState, payState);
        if (statusError != null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), statusError);
        }
        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid + "");
        if (orderExpress == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "订单快递不存在");
        }

        orderExpressMapper.updateOrderExpressStatusByUUID(uuid, routeState, payState);
        orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        return ApiUtil.getResponse(SUCCESS, orderExpress);
    }

    //////////////////// Private Method ////////////////////

    /**
     * 验证参数
     */
    private String verifyParamStatus(String routeState, String payState) {

        if (StringUtils.isBlank(routeState) || StringUtils.isBlank(payState)) {
            return "参数status不能为空";
        }
        String waitFill = "WAIT_FILL";
        if (!(waitFill.equals(routeState) ||
                "ALREADY_FILL".equals(routeState) ||
                "INIT".equals(routeState) ||
                "PAYING".equals(routeState) ||
                "WAIT_HAND_OVER".equals(routeState) ||
                "DELIVERING".equals(routeState) ||
                "FINISHED".equals(routeState) ||
                "ABNORMAL".equals(routeState) ||
                "CANCELED".equals(routeState) ||
                "REFUNDING".equals(routeState) ||
                "WAIT_PAY".equals(payState) ||
                "ALREADY_PAY".equals(payState) ||
                "WAIT_REFUND".equals(payState) ||
                "REFUNDED".equals(payState))) {
            return "参数status不正确";
        }

        return null;
    }

}
