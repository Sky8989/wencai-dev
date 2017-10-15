package com.sftc.web.service.impl.logic.app;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.APIGetUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.mybatis.EvaluateMapper;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.Evaluate;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
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
    @Resource
    private OrderExpressDao orderExpressDao;

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
                    myOrderParam.setKeyword_state((String) entry.getValue());
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
            } else {
                //两种状态值二选一
                myOrderParam.setKeyword(null);
            }
        }

        // pageNum -> startIndex
        myOrderParam.setPageNum((myOrderParam.getPageNum() - 1) * myOrderParam.getPageSize());
        // select
//        List<OrderDTO> orderList = orderMapper.selectMyOrderList(myOrderParam);
        List<OrderDTO> orderDTOList = orderMapper.selectMyOrderList2(myOrderParam);
        List<OrderCallback> orderCallbacks = new ArrayList<OrderCallback>();
        for (OrderDTO orderDTO : orderDTOList) {
            OrderCallback callback = new OrderCallback();
            // order
            callback.setId(orderDTO.getId());
            callback.setSender_name(orderDTO.getSender_name());
            callback.setSender_addr(orderDTO.getSender_addr());
            callback.setOrder_type(orderDTO.getOrder_type());
            callback.setRegion_type(orderDTO.getRegion_type());
            callback.setIs_gift(orderDTO.getGift_card_id() > 0);
            callback.setPay_method(orderDTO.getPay_method());
            if (orderDTO.getOrderExpressList().size() == 1) // 单包裹
                callback.setOrder_number(orderDTO.getOrderExpressList().get(0).getOrder_number());

            // expressList
            List<OrderCallback.OrderCallbackExpress> expressList = new ArrayList<OrderCallback.OrderCallbackExpress>();
            HashSet flagSetIsEvaluated = new HashSet();
            for (OrderExpress oe : orderDTO.getOrderExpressList()) {
                OrderCallback.OrderCallbackExpress express = new OrderCallback().new OrderCallbackExpress();
                express.setUuid(oe.getUuid());
                express.setState(oe.getState());
                express.setShip_name(oe.getShip_name());
                express.setShip_addr(oe.getShip_addr());
                express.setOrder_number(oe.getOrder_number());
                express.setPackage_type(oe.getPackage_type());  //增加包裹类型的三个字段
                express.setObject_type(oe.getObject_type());
                express.setPackage_comments(oe.getPackage_comments());
                express.setReserve_time(oe.getReserve_time());
                express.setDirected_code(oe.getDirected_code());
                //如果有异常信息，则添加异常信息
                if (oe.getAttributes() != null && !"".equals(oe.getAttributes()))
                    express.setAttributes((oe.getAttributes()));
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
        List<OrderDTO> orderDTOList = orderMapper.selectMyFriendOrderList(myOrderParam);
        List<OrderFriendCallback> orderCallbacks = new ArrayList<OrderFriendCallback>();
        for (OrderDTO orderDTO : orderDTOList) {
            OrderFriendCallback callback = new OrderFriendCallback();
            User sender = userMapper.selectUserByUserId(orderDTO.getSender_user_id());
            // order
            callback.setId(orderDTO.getId());
            callback.setSender_user_id(orderDTO.getSender_user_id());
            callback.setSender_name(orderDTO.getSender_name());
            if (sender != null && sender.getAvatar() != null) {
                callback.setSender_avatar(sender.getAvatar());
            }
            if (orderDTO.getOrderExpressList() != null && orderDTO.getOrderExpressList().size() > 0 && orderDTO.getOrderExpressList().get(0).getObject_type() != null && orderDTO.getOrderExpressList().get(0).getObject_type().length() > 0) { // powerful verify
                callback.setObject_type(orderDTO.getOrderExpressList().get(0).getObject_type());
            }
            callback.setWord_message(orderDTO.getWord_message());
            callback.setImage(orderDTO.getImage());
            callback.setCreate_time(orderDTO.getCreate_time());
            callback.setRegion_type(orderDTO.getRegion_type());
            callback.setIs_gift(orderDTO.getGift_card_id() > 0);
            //增加支付类型
            callback.setPay_method(orderDTO.getPay_method());
            // expressList
            List<OrderFriendCallback.OrderFriendCallbackExpress> expressList = new ArrayList<OrderFriendCallback.OrderFriendCallbackExpress>();
            HashSet flagSetIsEvaluated = new HashSet();
            for (OrderExpress oe : orderDTO.getOrderExpressList()) {
                User receiver = userMapper.selectUserByUserId(oe.getShip_user_id());
                OrderFriendCallback.OrderFriendCallbackExpress express = new OrderFriendCallback().new OrderFriendCallbackExpress();
                express.setId(oe.getId());
                express.setShip_user_id(oe.getShip_user_id());
                express.setUuid(oe.getUuid());
                express.setState(oe.getState());
                express.setShip_name(oe.getShip_name());
                express.setPackage_type(oe.getPackage_type());  //增加包裹类型的三个字段
                express.setObject_type(oe.getObject_type());
                express.setPackage_comments(oe.getPackage_comments());
                express.setReserve_time(oe.getReserve_time());

                //如果有异常信息，则添加异常信息
                if (oe.getAttributes() != null && !"".equals(oe.getAttributes()))
                    express.setAttributes((oe.getAttributes()));
                if (receiver != null && receiver.getAvatar() != null)
                    express.setShip_avatar(receiver.getAvatar());
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

    //////////////////// Private Method ////////////////////

    // 验参、同步顺丰快递订单状态
    private APIResponse syncSFExpressStatus(MyOrderParam myOrderParam) {

        // Verify params
        if (myOrderParam.getToken().length() == 0) {
            //内置token
            myOrderParam.setToken(COMMON_ACCESSTOKEN);
        } else if (myOrderParam.getId() == 0) {
            return APIUtil.paramErrorResponse("用户id不能为空");
        } else if (myOrderParam.getPageNum() < 1 || myOrderParam.getPageSize() < 1) {
            return APIUtil.paramErrorResponse("分页参数无效");
        }

        /// handle SF orders url
        //更新用户的同城订单，更新sf状态的订单数 >= 接口查询的
        List<OrderExpress> orderExpressList = selectOrderExpressListForStatusUpdate(myOrderParam);
        if (orderExpressList == null || orderExpressList.size() == 0) return null;
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
            //这个status的改动是因为是预约单 预约单支付后，派单前都是PAYING
            Order order = orderMapper.selectOrderDetailByUuid(orders.getUuid());
            if (order.getRegion_type() != null && order.getRegion_type().equals("REGION_SAME")) {
                String status = (orders.isPayed() && orders.getStatus().equals("PAYING") && order.getPay_method().equals("FREIGHT_PREPAID")) ? "WAIT_HAND_OVER" : orders.getStatus();
                OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(orders.getUuid());
                orderExpress.setState(status);
                if( orders.getAttributes()!=null){
                    orderExpress.setAttributes(orders.getAttributes());
                }
                orderExpressDao.save(orderExpress);
            }
        }

        return null;
    }

    // 根据分页参数和用户id 去查查找该用户的相关快递信息
    private List<OrderExpress> selectOrderExpressListForStatusUpdate(MyOrderParam myOrderParam) {
        // 查出该用户id相关的订单号 把同城的单抓出来
        PageHelper.startPage(myOrderParam.getPageNum(), myOrderParam.getPageSize());
        List<Integer> orderIdList = orderExpressMapper.selectOrderIdForsyncSFExpressStatus(myOrderParam.getId());
        if (orderIdList.size() == 0) return null;
        //使用mybatis的批量查询功能 将orderExpress查询出来
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForsyncSFExpressStatus(orderIdList);
        if (orderExpressList.size() == 0) return null;
        return orderExpressList;
    }
}
