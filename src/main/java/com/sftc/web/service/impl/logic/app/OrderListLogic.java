package com.sftc.web.service.impl.logic.app;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.sf.SfOrderHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.tools.utils.OrderStateSyncUtil;
import com.sftc.web.dao.mybatis.*;
import com.sftc.web.model.dto.FriendOrderListDTO;
import com.sftc.web.model.dto.MyOrderListDTO;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.Evaluate;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.entity.UserContact;
import com.sftc.web.model.vo.swaggerOrderRequest.MyOrderParamVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.sf.SfTokenHelper.COMMON_ACCESSTOKEN;

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

    //////////////////// Public Method ////////////////////

    /**
     * 我的订单列表
     */
    public ApiResponse getMyOrderList(MyOrderParamVO myOrderParamVO) {
        String userUuid = TokenUtils.getInstance().getUserUUID();
        myOrderParamVO.setUserUuid(userUuid);

        String accessToken = TokenUtils.getInstance().getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            accessToken = COMMON_ACCESSTOKEN;
        }
        myOrderParamVO.setToken(accessToken);

        if (StringUtils.isBlank(myOrderParamVO.getUserUuid())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "用户id不能为空");
        } else if (myOrderParamVO.getPageNum() < 1 || myOrderParamVO.getPageSize() < 1) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "分页参数无效");
        }
        List<OrderExpress> orderExpressList = selectOrderExpressListForStatusUpdate(myOrderParamVO);

        ApiResponse errorResponse = new OrderStateSyncUtil().syncOrderState(orderExpressList, accessToken, orderMapper, orderExpressMapper);
        if (errorResponse != null) {
            return errorResponse;
        }

        if (StringUtils.isNotBlank(myOrderParamVO.getKeyword())) {
            boolean flag = true;
            // 访问 状态模糊关键字 字典 匹配到对应关键字
            Map<String, String> map = SfOrderHelper.getKeywordMap();
            for (Map.Entry entry : map.entrySet()) {
                if (myOrderParamVO.getKeyword().equals(entry.getKey())) {
                    myOrderParamVO.setKeyword_state((String) entry.getValue());
                    flag = false;
                }
            }

            if (flag) {
                StringBuilder sb = new StringBuilder();
                sb.append("%");
                char[] keywords = myOrderParamVO.getKeyword().toCharArray();
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

        List<OrderDTO> orderDTOList = orderMapper.selectMyOrderList(myOrderParamVO);

        List<MyOrderListDTO> myOrderListDTOS = new ArrayList<>();

        for (OrderDTO orderDTO : orderDTOList) {
            MyOrderListDTO callback = new MyOrderListDTO();
            // order
            callback.setId(orderDTO.getId());
            callback.setSender_name(orderDTO.getSender_name());
            callback.setSender_addr(orderDTO.getSender_addr());
            callback.setOrder_type(orderDTO.getOrder_type());
            callback.setIs_gift(orderDTO.getGift_card_id() > 0);
            callback.setPay_method(orderDTO.getPay_method());
            // 单包裹
            if (orderDTO.getOrderExpressList().size() == 1) {
                callback.setOrder_number(orderDTO.getOrderExpressList().get(0).getOrder_number());
            }

            List<MyOrderListDTO.OrderCallbackExpress> expressList = new ArrayList<>();
            HashSet<Integer> flagSetIsEvaluated = new HashSet<>();
            for (OrderExpress oe : orderDTO.getOrderExpressList()) {
                MyOrderListDTO.OrderCallbackExpress express = new MyOrderListDTO().new OrderCallbackExpress();
                express.setUuid(oe.getUuid());
                express.setRoute_state(oe.getRoute_state());
                express.setPay_state(oe.getPay_state());
                express.setShip_name(oe.getShip_name());
                express.setShip_addr(oe.getShip_addr());
                express.setOrder_number(oe.getOrder_number());
                express.setWeight(oe.getWeight());
                express.setObject_type(oe.getObject_type());
                express.setPackage_comments(oe.getPackage_comments());
                express.setReserve_time(oe.getReserve_time());
                express.setDirected_code(oe.getDirected_code());
                express.setPackage_type(oe.getPackage_type());
                //如果有异常信息，则添加异常信息
                if (StringUtils.isNotBlank(oe.getAttributes())) {
                    express.setAttributes((oe.getAttributes()));
                }
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

        return ApiUtil.getResponse(SUCCESS, myOrderListDTOS);
    }

    /**
     * 我的好友圈订单列表
     */
    public ApiResponse getMyFriendCircleOrderList(MyOrderParamVO myOrderParamVO) {
        String userUuid = TokenUtils.getInstance().getUserUUID();
        myOrderParamVO.setUserUuid(userUuid);

        String accessToken = TokenUtils.getInstance().getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            accessToken = COMMON_ACCESSTOKEN;
        }
        myOrderParamVO.setToken(accessToken);

        if (StringUtils.isBlank(myOrderParamVO.getUserUuid())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "用户id不能为空");
        } else if (myOrderParamVO.getPageNum() < 1 || myOrderParamVO.getPageSize() < 1) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "分页参数无效");
        }

        //更新用户的同城订单，更新sf状态的订单数 >= 接口查询的
        List<OrderExpress> orderExpressList = selectOrderExpressListForStatusUpdate(myOrderParamVO);

        ApiResponse errorResponse = new OrderStateSyncUtil().syncOrderState(orderExpressList, accessToken, orderMapper, orderExpressMapper);
        if (errorResponse != null) {
            return errorResponse;
        }

        // pageNum -> startIndex
        myOrderParamVO.setPageNum((myOrderParamVO.getPageNum() - 1) * myOrderParamVO.getPageSize());
        // select
        List<OrderDTO> orderDTOList = orderMapper.selectMyFriendOrderList(myOrderParamVO);
        if (orderDTOList.size() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "暂无好友订单");
        }
        List<FriendOrderListDTO> orderCallbacks = new ArrayList<>();
        for (OrderDTO orderDTO : orderDTOList) {
            FriendOrderListDTO callback = new FriendOrderListDTO();
            User sender = userMapper.selectUserByUserUUId(orderDTO.getSender_user_uuid());
            // order
            callback.setId(orderDTO.getId());
            callback.setSender_user_uuid(orderDTO.getSender_user_uuid());
            callback.setSender_name(orderDTO.getSender_name());
            if (sender != null && sender.getAvatar() != null) {
                callback.setSender_avatar(sender.getAvatar());
            }
            if (orderDTO.getOrderExpressList() != null && orderDTO.getOrderExpressList().size() > 0 &&
                    orderDTO.getOrderExpressList().get(0).getObject_type() != null &&
                    orderDTO.getOrderExpressList().get(0).getObject_type().length() > 0) {
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
            HashSet<Integer> flagSetIsEvaluated = new HashSet<>();
            for (OrderExpress oe : orderDTO.getOrderExpressList()) {
                User receiver = userMapper.selectUserByUserUUId(oe.getShip_user_uuid());
                //好友圈也需要好友关系id
                int userContactId = 0;
                UserContact userContact;
                String senderWxName = null;
                String shipWxName = null;
                if (StringUtils.isNotBlank(orderDTO.getSender_user_uuid()) && StringUtils.isNotBlank(oe.getShip_user_uuid())) {
                    //如果为寄件方
                    if (userUuid.equals(orderDTO.getSender_user_uuid())) {
                        userContact = userContactMapper.friendDetail(orderDTO.getSender_user_uuid(), oe.getShip_user_uuid());
                    } else {//如果为收件方
                        userContact = userContactMapper.friendDetail(oe.getShip_user_uuid(), orderDTO.getSender_user_uuid());
                    }
                    User senderName = userMapper.selectUserByUserUUId(oe.getSender_user_uuid());
                    User receiverName = userMapper.selectUserByUserUUId(oe.getShip_user_uuid());
                    if (sender != null) {
                        senderWxName = senderName.getName();
                    }
                    if (receiver != null) {
                        shipWxName = receiverName.getName();
                    }
                    callback.setSender_wx_name(senderWxName);
                    userContactId = userContact == null ? 0 : userContact.getId();
                }
                FriendOrderListDTO.OrderFriendCallbackExpress express = new FriendOrderListDTO().new OrderFriendCallbackExpress();
                express.setId(oe.getId());
                express.setShip_user_uuid(oe.getShip_user_uuid());
                express.setUuid(oe.getUuid());
                express.setOrder_number(oe.getOrder_number());
                express.setRoute_state(oe.getRoute_state());
                express.setPay_state(oe.getPay_state());
                express.setShip_name(oe.getShip_name());
                express.setShip_wx_name(shipWxName);
                express.setWeight(oe.getWeight());
                express.setObject_type(oe.getObject_type());
                express.setPackage_comments(oe.getPackage_comments());
                express.setReserve_time(oe.getReserve_time());
                express.setPackage_type(oe.getPackage_type());
                express.setUser_contact_id(userContactId);
                //如果有异常信息，则添加异常信息
                if (StringUtils.isNotBlank(oe.getAttributes())) {
                    express.setAttributes((oe.getAttributes()));
                }
                if (receiver != null && receiver.getAvatar() != null) {
                    express.setShip_avatar(receiver.getAvatar());
                }
                expressList.add(express);
                // 检查快递是否评价过
                List<Evaluate> evaluateList = evaluateMapper.selectByUuid(oe.getUuid());
                // 如果被评价过，且有评价信息，则返回1 如果无评价信息 则返回
                flagSetIsEvaluated.add((evaluateList.size() == 0) ? 0 : 1);
            }
            callback.setExpressList(expressList);
            callback.setIs_evaluated(flagSetIsEvaluated.contains(1));

            orderCallbacks.add(callback);
        }

        return ApiUtil.getResponse(SUCCESS, orderCallbacks);
    }

    /**
     * 根据分页参数和用户id 去查查找该用户的相关快递信息
     */
    private List<OrderExpress> selectOrderExpressListForStatusUpdate(MyOrderParamVO myOrderParamVO) {
        // 查出该用户id相关的订单号 把同城的单抓出来
        PageHelper.startPage(myOrderParamVO.getPageNum(), myOrderParamVO.getPageSize());
        List<String> orderIdList = orderExpressMapper.selectOrderIdForsyncSFExpressStatus(myOrderParamVO.getUserUuid());
        if (orderIdList.size() == 0) {
            return null;
        }
        //使用mybatis的批量查询功能 将orderExpress查询出来
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForsyncSFExpressStatus(orderIdList);
        if (orderExpressList.size() == 0) {
            return null;
        }
        return orderExpressList;
    }
}
