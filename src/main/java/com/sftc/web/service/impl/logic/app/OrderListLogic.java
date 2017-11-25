package com.sftc.web.service.impl.logic.app;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.APIGetUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.mybatis.*;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.*;
import com.sftc.web.model.dto.FriendOrderListDTO;
import com.sftc.web.model.dto.MyOrderListDTO;
import com.sftc.web.model.vo.swaggerOrderRequest.MyOrderParamVO;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderSynVO;
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
    private UserContactMapper userContactMapper;
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
        Integer user_id = TokenUtils.getInstance().getUserId();
        MyOrderParamVO myOrderParamVO = (MyOrderParamVO) request.getRequestParam();
        myOrderParamVO.setId(user_id);
        APIResponse errorResponse = syncSFExpressStatus(myOrderParamVO);
        if (errorResponse != null) return errorResponse;

        if (myOrderParamVO.getKeyword() != null && !myOrderParamVO.getKeyword().equals("")) {
            boolean flag = true;
            // 访问 状态模糊关键字 字典 匹配到对应关键字
            Map<String, String> map = SFOrderHelper.getKeywordMap();
            for (Map.Entry entry : map.entrySet()) {
                if (myOrderParamVO.getKeyword().equals(entry.getKey())) {
                    myOrderParamVO.setKeyword_state((String) entry.getValue());
                    flag = false;
                }
            }

            if (flag) {
                StringBuilder sb = new StringBuilder();
                sb.append("%");
                char keywords[] = myOrderParamVO.getKeyword().toCharArray();
                for (char key : keywords) {
                    sb.append(key);
                    sb.append("%");
                }
                myOrderParamVO.setKeyword(sb.toString());
            } else {
                //两种状态值二选一
                myOrderParamVO.setKeyword(null);
            }
        }

        // pageNum -> startIndex
        myOrderParamVO.setPageNum((myOrderParamVO.getPageNum() - 1) * myOrderParamVO.getPageSize());
        // select
//        List<OrderDTO> orderList = orderMapper.selectMyOrderList(myOrderParamVO);
        List<OrderDTO> orderDTOList = orderMapper.selectMyOrderList2(myOrderParamVO);
        if (orderDTOList.size() == 0)
            return APIUtil.selectErrorResponse("您还未创建订单", null);
        List<MyOrderListDTO> myOrderListDTOS = new ArrayList<MyOrderListDTO>();
        for (OrderDTO orderDTO : orderDTOList) {
            MyOrderListDTO callback = new MyOrderListDTO();
            // order
            callback.setId(orderDTO.getId());
            callback.setSender_name(orderDTO.getSender_name());
            callback.setSender_addr(orderDTO.getSender_addr());
            callback.setOrder_type(orderDTO.getOrder_type());
            callback.setIs_gift(orderDTO.getGift_card_id() > 0);
            callback.setPay_method(orderDTO.getPay_method());
            if (orderDTO.getOrderExpressList().size() == 1) // 单包裹
                callback.setOrder_number(orderDTO.getOrderExpressList().get(0).getOrder_number());

            // expressList
            List<MyOrderListDTO.OrderCallbackExpress> expressList = new ArrayList<MyOrderListDTO.OrderCallbackExpress>();
            HashSet flagSetIsEvaluated = new HashSet();
            for (OrderExpress oe : orderDTO.getOrderExpressList()) {
                MyOrderListDTO.OrderCallbackExpress express = new MyOrderListDTO().new OrderCallbackExpress();
                express.setUuid(oe.getUuid());
                express.setRoute_state(oe.getRoute_state());
                express.setPay_state(oe.getPay_state());
                express.setShip_name(oe.getShip_name());
                express.setShip_addr(oe.getShip_addr());
                express.setOrder_number(oe.getOrder_number());
                express.setWeight(oe.getWeight());  //增加包裹类型的三个字段
                express.setObject_type(oe.getObject_type());
                express.setPackage_comments(oe.getPackage_comments());
                express.setReserve_time(oe.getReserve_time());
                express.setDirected_code(oe.getDirected_code());
                express.setPackage_type(oe.getPackage_type());
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

            myOrderListDTOS.add(callback);
        }

        return APIUtil.getResponse(SUCCESS, myOrderListDTOS);
    }

    /**
     * 我的好友圈订单列表
     */
    public APIResponse getMyFriendCircleOrderList(APIRequest request) {
        Integer user_id = TokenUtils.getInstance().getUserId();
        MyOrderParamVO myOrderParamVO = (MyOrderParamVO) request.getRequestParam();
        myOrderParamVO.setId(user_id);

        APIResponse errorResponse = syncSFExpressStatus(myOrderParamVO);
        if (errorResponse != null) return errorResponse;

        // pageNum -> startIndex
        myOrderParamVO.setPageNum((myOrderParamVO.getPageNum() - 1) * myOrderParamVO.getPageSize());
        // select
        List<OrderDTO> orderDTOList = orderMapper.selectMyFriendOrderList(myOrderParamVO);
        if (orderDTOList.size() == 0)
            return APIUtil.selectErrorResponse("暂无好友订单", null);
        List<FriendOrderListDTO> orderCallbacks = new ArrayList<>();
        for (OrderDTO orderDTO : orderDTOList) {
            FriendOrderListDTO callback = new FriendOrderListDTO();
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
            callback.setIs_gift(orderDTO.getGift_card_id() > 0);
            //增加支付类型
            callback.setPay_method(orderDTO.getPay_method());
            // expressList
            List<FriendOrderListDTO.OrderFriendCallbackExpress> expressList = new ArrayList<>();
            HashSet flagSetIsEvaluated = new HashSet();
            for (OrderExpress oe : orderDTO.getOrderExpressList()) {
                User receiver = userMapper.selectUserByUserId(oe.getShip_user_id());
                int user_contact_id = 0;//好友圈也需要好友关系id
                UserContact userContact = null;
                if (orderDTO.getSender_user_id() != 0 && oe.getShip_user_id() != 0) {
                    if (user_id == orderDTO.getSender_user_id()) {//如果为寄件方
                        userContact = userContactMapper.friendDetail(orderDTO.getSender_user_id(), oe.getShip_user_id());
                    } else {//如果为收件方
                        userContact = userContactMapper.friendDetail(oe.getShip_user_id(), orderDTO.getSender_user_id());
                    }
                    if (userContact == null) user_contact_id = 0;
                    else user_contact_id = userContact.getId();
                }
                FriendOrderListDTO.OrderFriendCallbackExpress express = new FriendOrderListDTO().new OrderFriendCallbackExpress();
                express.setId(oe.getId());
                express.setShip_user_id(oe.getShip_user_id());
                express.setUuid(oe.getUuid());
                express.setRoute_state(oe.getRoute_state());
                express.setPay_state(oe.getPay_state());
                express.setShip_name(oe.getShip_name());
                express.setWeight(oe.getWeight());  //增加包裹类型的三个字段
                express.setObject_type(oe.getObject_type());
                express.setPackage_comments(oe.getPackage_comments());
                express.setReserve_time(oe.getReserve_time());
                express.setPackage_type(oe.getPackage_type());
                express.setUser_contact_id(user_contact_id);
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
    private APIResponse syncSFExpressStatus(MyOrderParamVO myOrderParamVO) {

        // Verify params
//        if (myOrderParamVO.getToken().length() == 0) {
        //内置token
        myOrderParamVO.setToken(COMMON_ACCESSTOKEN);
        if (myOrderParamVO.getId() == 0) {
            return APIUtil.paramErrorResponse("用户id不能为空");
        } else if (myOrderParamVO.getPageNum() < 1 || myOrderParamVO.getPageSize() < 1) {
            return APIUtil.paramErrorResponse("分页参数无效");
        }

        /// handle SF orders url
        //更新用户的同城订单，更新sf状态的订单数 >= 接口查询的
        List<OrderExpress> orderExpressList = selectOrderExpressListForStatusUpdate(myOrderParamVO);
        if (orderExpressList == null || orderExpressList.size() == 0) return null;
        StringBuilder uuidSB = new StringBuilder();
        for (OrderExpress oe : orderExpressList) {
            if (oe.getUuid() != null && oe.getUuid().length() != 0) {
                Order order = orderMapper.selectOrderDetailByOrderId(oe.getOrder_id());
                if (order != null && oe.getOrder_number() != null && !oe.getOrder_number().equals("")) { // 只有同城的订单能同步快递状态
                    uuidSB.append(oe.getUuid());
                    uuidSB.append(",");
                }
                if (oe.getOrder_number() == null || oe.getOrder_number().equals("")) {
                    orderExpressMapper.updatePayState("WAIT_PAY",oe.getUuid());
                }
            }
        }
        String uuid = uuidSB.toString();
        if (uuid.equals("")) return null;

        String ordersURL = SF_ORDER_SYNC_URL.replace("{uuid}", uuid.substring(0, uuid.length() - 1));
        List<OrderSynVO> orderSynVOList = new LinkedList<OrderSynVO>();
        //post and fetch express status list
        HttpGet httpGet = new HttpGet(ordersURL);
        httpGet.addHeader("PushEnvelope-Device-Token", myOrderParamVO.getToken());
        String resultStr = APIGetUtil.get(httpGet);
        JSONObject resultOBJ = JSONObject.fromObject(resultStr);

        if (resultOBJ.containsKey("error")) return APIUtil.selectErrorResponse("sf error", resultOBJ);
        // 多个uuid 返回requests
        if (resultOBJ.containsKey("requests")) {
            JSONArray jsonArray = resultOBJ.getJSONArray("requests");
            List list = jsonArray.subList(0, jsonArray.size());
            for (Object temp : list) {
                JSONObject jsonObject = JSONObject.fromObject(temp);
                OrderSynVO tempOrderSynVO = new OrderSynVO();
                tempOrderSynVO.setUuid(jsonObject.getString("uuid"));
                tempOrderSynVO.setStatus(jsonObject.getString("status"));
                tempOrderSynVO.setPayed(jsonObject.getBoolean("payed"));
                JSONObject attributes = jsonObject.getJSONObject("attributes");
                tempOrderSynVO.setAttributes(attributes.toString());
                orderSynVOList.add(tempOrderSynVO);
            }
        }
        // 单个uudi 返回request
        if (resultOBJ.containsKey("request")) {
            orderSynVOList.add((OrderSynVO) resultOBJ.get("request"));
        }


        // Update Dankal express info
        for (OrderSynVO orderSynVO : orderSynVOList) {
            // 已支付的订单，如果status为PAYING，则要改为WAIT_HAND_OVER
            //这个status的改动是因为是预约单 预约单支付后，派单前都是PAYING
            Order order = orderMapper.selectOrderDetailByUuid(orderSynVO.getUuid());
            String status = (orderSynVO.isPayed() && orderSynVO.getStatus().equals("PAYING") && order.getPay_method().equals("FREIGHT_PREPAID")) ? "WAIT_HAND_OVER" : orderSynVO.getStatus();
            String pay_state = "WAIT_PAY";
            if (orderSynVO.getStatus().equals("WAIT_REFUND")) { //待退款、已退款路由状态合并为已取消
                status = "CANCELED";
                pay_state = "WAIT_REFUND";
            }
            if (orderSynVO.getStatus().equals("REFUNDED")) { //待退款、已退款路由状态合并为已取消
                status = "CANCELED";
                pay_state = "REFUNDED";
            }
            if (!status.equals("CANCELED")) pay_state = orderSynVO.isPayed() ? "ALREADY_PAY" : "WAIT_PAY";

            //存在锁的问题，修改语句改为一条
            String attributes = orderSynVO.getAttributes();
            orderExpressMapper.updateAttributesAndStatusByUUID(orderSynVO.getUuid(), attributes, status, pay_state);
        }

        return null;
    }

    // 根据分页参数和用户id 去查查找该用户的相关快递信息
    private List<OrderExpress> selectOrderExpressListForStatusUpdate(MyOrderParamVO myOrderParamVO) {
        // 查出该用户id相关的订单号 把同城的单抓出来
        PageHelper.startPage(myOrderParamVO.getPageNum(), myOrderParamVO.getPageSize());
        List<Integer> orderIdList = orderExpressMapper.selectOrderIdForsyncSFExpressStatus(myOrderParamVO.getId());
        if (orderIdList.size() == 0) return null;
        //使用mybatis的批量查询功能 将orderExpress查询出来
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForsyncSFExpressStatus(orderIdList);
        if (orderExpressList.size() == 0) return null;
        return orderExpressList;
    }
}
