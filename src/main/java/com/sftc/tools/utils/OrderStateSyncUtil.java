package com.sftc.tools.utils;

import com.sftc.tools.api.ApiGetUtil;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderSynVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;

import java.util.LinkedList;
import java.util.List;

import static com.sftc.tools.constant.SfConstant.SF_ORDER_SYNC_URL;

/**
 * 路由状态同步工具类
 *
 * @author ： xfan
 * @date ：Create in 11:51 2017/11/29
 */
public class OrderStateSyncUtil {

    public ApiResponse syncOrderState(List<OrderExpress> orderExpressList, String accessToken,
                                      OrderMapper orderMapper, OrderExpressMapper orderExpressMapper) {

        /*------------------------------------------------------------ 需要同步的快递 uuid 的获取 --------------------------------------------------------------------------*/
        if (orderExpressList == null || orderExpressList.size() == 0) {
            return null;
        }
        StringBuilder uuidSB = new StringBuilder();
        for (OrderExpress oe : orderExpressList) {
            if (oe.getUuid() != null && oe.getUuid().length() != 0) {
                Order order = orderMapper.selectOrderDetailByOrderId(oe.getOrder_id());
                if (order != null && oe.getOrder_number() != null && !oe.getOrder_number().equals("")) {
                    uuidSB.append(oe.getUuid());
                    uuidSB.append(",");
                }
                if (oe.getOrder_number() == null || oe.getOrder_number().equals("")) {
                    orderExpressMapper.updatePayState("WAIT_PAY", oe.getUuid());
                }
            }
        }
        String uuid = uuidSB.toString();
        if ("".equals(uuid)) {
            return null;
        }

        /*------------------------------------------------------------ sf请求 同步快递路由信息 --------------------------------------------------------------------------*/
        String ordersURL = SF_ORDER_SYNC_URL.replace("{uuid}", uuid.substring(0, uuid.length() - 1));
        List<OrderSynVO> orderSynVOList = new LinkedList<OrderSynVO>();
        //post and fetch express status list
        HttpGet httpGet = new HttpGet(ordersURL);
        httpGet.addHeader("PushEnvelope-Device-Token", accessToken);
        String resultStr = ApiGetUtil.get(httpGet);
        JSONObject resultOBJ = JSONObject.fromObject(resultStr);

        if (resultOBJ.containsKey("error")) {
            return ApiUtil.selectErrorResponse("sf error", resultOBJ);
        }
        // 多个uuid 返回requests
        if (resultOBJ.containsKey("requests")) {
            JSONArray jsonArray = resultOBJ.getJSONArray("requests");
            @SuppressWarnings("rawtypes")
            List list = jsonArray.subList(0, jsonArray.size());
            for (Object temp : list) {
                JSONObject jsonObject = JSONObject.fromObject(temp);
                OrderSynVO tempOrderSynVO = new OrderSynVO();
                tempOrderSynVO.setUuid(jsonObject.getString("uuid"));
                tempOrderSynVO.setStatus(jsonObject.getString("status"));
                tempOrderSynVO.setPayed(jsonObject.getBoolean("payed"));
                JSONObject attributes = jsonObject.getJSONObject("attributes");
                tempOrderSynVO.setAttributes(attributes);
                orderSynVOList.add(tempOrderSynVO);
            }
        }
        // 单个uuid 返回request
        if (resultOBJ.containsKey("request")) {
            orderSynVOList.add((OrderSynVO) resultOBJ.get("request"));
        }

        /*------------------------------------------------------------ 返回 request 信息的处理 --------------------------------------------------------------------------*/
        for (OrderSynVO orderSynVO : orderSynVOList) {
            // 已支付的订单，如果status为PAYING，则要改为WAIT_HAND_OVER (这个status的改动是因为是预约单 预约单支付后，派单前都是PAYING)
            Order order = orderMapper.selectOrderDetailByUuid(orderSynVO.getUuid());
            String status = (orderSynVO.isPayed() && "PAYING".equals(orderSynVO.getStatus()) && "FREIGHT_PREPAID".equals(order.getPay_method())) ? "WAIT_HAND_OVER" : orderSynVO.getStatus();
            //到付订单，如果状态为PAYING 则要改为派送中 DELIVERING
            if (order.getPay_method().equals("FREIGHT_COLLECT")) {
                status = (orderSynVO.getStatus().equals("PAYING")) ? "DELIVERING" : orderSynVO.getStatus();
            }
            String payState = orderSynVO.isPayed() ? "ALREADY_PAY" : "WAIT_PAY";

            //同步面对面订单的信息  取件码 directed_code 是否面对面 is_directed
            String directedCode = null;
            OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(orderSynVO.getUuid());
            int isDirected = StringUtils.isBlank(orderExpress.getDirected_code()) ? 0 : 1;
            if (orderSynVO.getAttributes() != null) {
                JSONObject attributesOBJ = orderSynVO.getAttributes();
                if (attributesOBJ != null) {
                    if (attributesOBJ.containsKey("directed_code")) {
                        directedCode = attributesOBJ.getString("directed_code");
                        isDirected = 1;
                    } else {
                        isDirected = 0;
                    }
                }
            }
            orderExpressMapper.updateDirectedMessage(directedCode, isDirected, orderSynVO.getUuid());

            //异常件 ABNORMAL 的处理 根据异常信息匹配路由状态返回
            JSONObject attributesOBJ = orderSynVO.getAttributes();
            String attributes = attributesOBJ.toString();
            if (status.equals("ABNORMAL")) {
                if (attributesOBJ.containsKey("abnormal_option")) {
                    String abNormalError = attributesOBJ.getString("abnormal_option");
                    if (abNormalError != null && abNormalError.equals("CUSTOMER_CANCEL") ||
                            abNormalError.equals("CONTACT_CUSTOMER_FAILURE") || abNormalError.equals("ERROR_CUSTOMER_ADDRESS") ||
                            abNormalError.equals("CONFORM_TO_ORDER_FAILURE") || abNormalError.equals("PICK_UP_OTHERS") ||
                            abNormalError.equals("DISPATCH_TIME_OUT")) {
                        status = "CANCELED";
                    } else if (abNormalError != null && abNormalError.equals("CUSTOMER_REJECTION") ||
                            abNormalError.equals("CONTACT_COURIER_FAILURE") || abNormalError.equals("CONTACT_RECEIVER_FAILURE") ||
                            abNormalError.equals("ERROR_RECEIVER_ADDRESS") || abNormalError.equals("TO_DROP_OFF_OTHERS") ||
                            abNormalError.equals("PAY_FAILURE") || abNormalError.equals("VERIFY_FAILURE")) {
                        status = "DELIVERING";
                    } else if (abNormalError != null && abNormalError.equals("UNABLE_TO_PICK_UP") ||
                            abNormalError.equals("DISPATCH_FAILED") || abNormalError.equals("DISCARD_TRIP_GROUP")) {
                        status = "WAIT_HAND_OVER";
                    }
                }
            }
            //当顺丰状态落后于我们数据库状态时，状态不同步
            if (order.getPay_method().equals("FREIGHT_COLLECT") && status.equals("INIT") && orderExpress.getRoute_state().equals("WAIT_HAND_OVER")) {
                status = "WAIT_HAND_OVER";
            }
            orderExpressMapper.updateAttributesAndStatusByUUID(orderSynVO.getUuid(), attributes, status, payState);
        }
        return null;
    }
}
