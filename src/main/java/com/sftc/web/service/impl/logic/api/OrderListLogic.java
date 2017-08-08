package com.sftc.web.service.impl.logic.api;

import com.google.gson.JsonArray;
import com.sftc.tools.api.*;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.web.mapper.EvaluateMapper;
import com.sftc.web.mapper.OrderExpressMapper;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.Evaluate;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.User;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.apiCallback.OrderFriendCallback;
import com.sftc.web.model.reqeustParam.MyOrderParam;
import com.sftc.web.model.sfmodel.Orders;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_ORDER_SYNC_URL;
import static com.sftc.tools.sf.SFTokenHelper.COMMON_ACCESSTOKEN;

@Component
public class OrderListLogic {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private EvaluateMapper evaluateMapper;

    //////////////////// Public Method ////////////////////

    /**
     * 我的订单列表
     */
    public APIResponse getMyOrderList(APIRequest request) {
        MyOrderParam myOrderParam = (MyOrderParam) request.getRequestParam();

        APIResponse errorResponse = syncSFExpressStatus(myOrderParam);
        if (errorResponse != null) return errorResponse;

        if (myOrderParam.getKeyword() != null && !myOrderParam.getKeyword().equals("")) {
            boolean flag = true;
            // 访问 状态模糊关键字 字典 匹配到对应关键字
            Map<String, String> map = SFOrderHelper.getKeywordMap();
            for (Map.Entry entry : map.entrySet()) {
                if (myOrderParam.getKeyword().equals(entry.getKey())) {
                    myOrderParam.setKeyword((String) entry.getValue());
                    flag = false;
                }
            }

            if (flag) {
                StringBuilder sb = new StringBuilder();
                sb.append("%");
                char keywords[] = myOrderParam.getKeyword().toCharArray();
                for (char key : keywords) {
                    sb.append(key);
                    sb.append("%");
                }
                myOrderParam.setKeyword(sb.toString());
            }
        }

        // pageNum -> startIndex
        myOrderParam.setPageNum((myOrderParam.getPageNum() - 1) * myOrderParam.getPageSize());
        // select
        List<Order> orderList = orderMapper.selectMyOrderList(myOrderParam);
        List<OrderCallback> orderCallbacks = new ArrayList<OrderCallback>();
        for (Order order : orderList) {
            OrderCallback callback = new OrderCallback();
            // order
            callback.setId(order.getId());
            callback.setSender_name(order.getSender_name());
            callback.setSender_addr(order.getSender_addr());
            callback.setOrder_type(order.getOrder_type());
            callback.setRegion_type(order.getRegion_type());
            callback.setIs_gift(order.getGift_card_id() > 0);
            callback.setPay_method(order.getPay_method());
            if (order.getOrderExpressList().size() == 1) // 单包裹
                callback.setOrder_number(order.getOrderExpressList().get(0).getOrder_number());

            // expressList
            List<OrderCallback.OrderCallbackExpress> expressList = new ArrayList<OrderCallback.OrderCallbackExpress>();
            HashSet flagSetIsEvaluated = new HashSet();
            for (OrderExpress oe : order.getOrderExpressList()) {
                OrderCallback.OrderCallbackExpress express = new OrderCallback().new OrderCallbackExpress();
                express.setUuid(oe.getUuid());
                express.setState(oe.getState());
                express.setShip_name(oe.getShip_name());
                express.setShip_addr(oe.getShip_addr());
                express.setOrder_number(oe.getOrder_number());
                //如果有异常信息，则添加异常信息
                if (oe.getAttributes() != null && !"".equals(oe.getAttributes()))
                    express.setAttributes(JSONObject.fromObject(oe.getAttributes()));
                expressList.add(express);
                // 检查快递是否评价过
                List<Evaluate> evaluateList = evaluateMapper.selectByUuid(oe.getUuid());
                // 如果被评价过，且有评价信息，则返回1 如果无评价信息 则返回0
                flagSetIsEvaluated.add((evaluateList.size() == 0) ? 0 : 1);
            }
            callback.setExpressList(expressList);
            callback.setIs_evaluated(flagSetIsEvaluated.contains(1));

            orderCallbacks.add(callback);
        }

        return APIUtil.getResponse(SUCCESS, orderCallbacks);
    }

    /**
     * 我的好友圈订单列表
     */
    public APIResponse getMyFriendCircleOrderList(APIRequest request) {
        MyOrderParam myOrderParam = (MyOrderParam) request.getRequestParam();

        APIResponse errorResponse = syncSFExpressStatus(myOrderParam);
        if (errorResponse != null) return errorResponse;

        // pageNum -> startIndex
        myOrderParam.setPageNum((myOrderParam.getPageNum() - 1) * myOrderParam.getPageSize());
        // select
        List<Order> orderList = orderMapper.selectMyFriendOrderList(myOrderParam);
        List<OrderFriendCallback> orderCallbacks = new ArrayList<OrderFriendCallback>();
        for (Order order : orderList) {
            OrderFriendCallback callback = new OrderFriendCallback();
            User sender = userMapper.selectUserByUserId(order.getSender_user_id());
            // order
            callback.setId(order.getId());
            callback.setSender_user_id(order.getSender_user_id());
            callback.setSender_name(order.getSender_name());
            if (sender != null && sender.getAvatar() != null) {
                callback.setSender_avatar(sender.getAvatar());
            }
            if (order.getOrderExpressList() != null && order.getOrderExpressList().size() > 0 && order.getOrderExpressList().get(0).getObject_type().length() > 0) { // powerful verify
                callback.setObject_type(order.getOrderExpressList().get(0).getObject_type());
            }
            callback.setWord_message(order.getWord_message());
            callback.setImage(order.getImage());
            callback.setCreate_time(order.getCreate_time());
            callback.setRegion_type(order.getRegion_type());
            callback.setIs_gift(order.getGift_card_id() > 0);
            // expressList
            List<OrderFriendCallback.OrderFriendCallbackExpress> expressList = new ArrayList<OrderFriendCallback.OrderFriendCallbackExpress>();
            for (OrderExpress oe : order.getOrderExpressList()) {
                User receiver = userMapper.selectUserByUserId(oe.getShip_user_id());
                OrderFriendCallback.OrderFriendCallbackExpress express = new OrderFriendCallback().new OrderFriendCallbackExpress();
                express.setId(oe.getId());
                express.setShip_user_id(oe.getShip_user_id());
                express.setUuid(oe.getUuid());
                express.setState(oe.getState());
                express.setShip_name(oe.getShip_name());
                //如果有异常信息，则添加异常信息
                if (oe.getAttributes() != null && !"".equals(oe.getAttributes()))
                    express.setAttributes(JSONObject.fromObject(oe.getAttributes()));
                if (receiver != null && receiver.getAvatar() != null)
                    express.setShip_avatar(receiver.getAvatar());
                expressList.add(express);
            }
            callback.setExpressList(expressList);

            orderCallbacks.add(callback);
        }

        return APIUtil.getResponse(SUCCESS, orderCallbacks);
    }

    //////////////////// Private Method ////////////////////

    // 验参、同步顺丰快递订单状态
    private APIResponse syncSFExpressStatus(MyOrderParam myOrderParam) {

        // Verify params
        if (myOrderParam.getToken().length() == 0) {
            //内置token
            myOrderParam.setToken(COMMON_ACCESSTOKEN);
            //return APIUtil.paramErrorResponse("token不能为空");
        } else if (myOrderParam.getId() == 0) {
            return APIUtil.paramErrorResponse("用户id不能为空");
        } else if (myOrderParam.getPageNum() < 1 || myOrderParam.getPageSize() < 1) {
            return APIUtil.paramErrorResponse("分页参数无效");
        }

        // handle SF orders url
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForId(myOrderParam.getId());
        StringBuilder uuidSB = new StringBuilder();
        for (OrderExpress oe : orderExpressList) {
            if (oe.getUuid() != null && oe.getUuid().length() != 0) {
                Order order = orderMapper.selectOrderDetailByOrderId(oe.getOrder_id());
                if (order != null && order.getRegion_type() != null && order.getRegion_type().equals("REGION_SAME")) { // 只有同城的订单能同步快递状态
                    uuidSB.append(oe.getUuid());
                    uuidSB.append(",");
                }
            }
        }
        String uuid = uuidSB.toString();
        if (uuid.equals("")) return null;

        String ordersURL = SF_ORDER_SYNC_URL.replace("{uuid}", uuid.substring(0, uuid.length() - 1));
        List<Orders> ordersList = new LinkedList<Orders>();
        //post and fetch express status list
        HttpGet httpGet = new HttpGet(ordersURL);
        httpGet.addHeader("PushEnvelope-Device-Token", myOrderParam.getToken());
        String resultStr = APIGetUtil.get(httpGet);
        JSONObject resultOBJ = JSONObject.fromObject(resultStr);

        if (resultOBJ.containsKey("error")) return APIUtil.selectErrorResponse("sf error", resultOBJ);
        // 多个uuid 返回requests
        if (resultOBJ.containsKey("requests")) {
            JSONArray jsonArray = resultOBJ.getJSONArray("requests");
            List list = jsonArray.subList(0, jsonArray.size());
            for (Object temp : list) {
                JSONObject jsonObject = JSONObject.fromObject(temp);
                Orders tempOrders = new Orders();
                tempOrders.setUuid(jsonObject.getString("uuid"));
                tempOrders.setStatus(jsonObject.getString("status"));
                tempOrders.setPayed(jsonObject.getBoolean("payed"));
                JSONObject attributes = jsonObject.getJSONObject("attributes");
                tempOrders.setAttributes(attributes.toString());
                ordersList.add(tempOrders);
            }
        }
        // 单个uudi 返回request
        if (resultOBJ.containsKey("request")) {
            ordersList.add((Orders) resultOBJ.get("request"));
        }


        // Update Dankal express info
        for (Orders orders : ordersList) {
            // 已支付的订单，如果status为PAYING，则要改为WAIT_HAND_OVER
            String status = orders.isPayed() && orders.getStatus().equals("PAYING") ? "WAIT_HAND_OVER" : orders.getStatus();
            orderExpressMapper.updateOrderExpressForSF(new OrderExpress(status, orders.getUuid(), orders.getAttributes()));
        }

        return null;
    }
}
