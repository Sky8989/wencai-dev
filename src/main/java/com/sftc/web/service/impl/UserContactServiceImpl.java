package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResolve;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.*;
import com.sftc.web.model.dto.FriendRecordDTO;
import com.sftc.web.model.entity.*;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderSynVO;
import com.sftc.web.model.vo.swaggerRequest.FriendListVO;
import com.sftc.web.model.vo.swaggerRequest.UserContactParamVO;
import com.sftc.web.service.UserContactService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.Object;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_ORDER_SYNC_URL;
import static com.sftc.tools.sf.SFTokenHelper.COMMON_ACCESSTOKEN;

@Service("userContactService")
public class UserContactServiceImpl implements UserContactService {

    @Resource
    private UserContactMapper userContactMapper;

    @Resource
    private UserContactLabelMapper userContactLabelMapper;

    @Resource
    private OrderExpressMapper orderExpressMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    /*
     * 根据id查询好友详情
     */
    public APIResponse getFriendDetail(APIRequest request) {
        String friendId = (String) request.getParameter("friend_id");

        if (friendId == null || friendId.equals(""))
            return APIUtil.paramErrorResponse("friend_id不能为空");

        Integer user_id = TokenUtils.getInstance().getUserId();
        int friend_id = Integer.parseInt(friendId);

        if (user_id < 1)
            return APIUtil.paramErrorResponse("user_id无效");
        if (friend_id < 1)
            return APIUtil.paramErrorResponse("friend_id无效");

        UserContact userContact = userContactMapper.friendDetail(user_id, friend_id);

        if (userContact == null)
            return APIUtil.selectErrorResponse("非好友关系", null);

        List<UserContactLabel> userContactLabelList = userContactLabelMapper.getFriendLabelList(user_id);
        userContact.setUserContactLabelList(userContactLabelList);
        return APIUtil.getResponse(SUCCESS, userContact);
    }

    /**
     * 获取用户的好友列表
     */
    public APIResponse getFriendList(APIRequest request) {
        FriendListVO friendListVO = (FriendListVO) request.getRequestParam();
        Integer user_id = TokenUtils.getInstance().getUserId();
        // verify params
        if (friendListVO.getPageNum() < 1 || friendListVO.getPageSize() < 1) {
            return APIUtil.paramErrorResponse("分页参数无效");
        }
        friendListVO.setUser_id(user_id);
        friendListVO.setPageNum((friendListVO.getPageNum() - 1) * friendListVO.getPageSize());
        List<UserContact> userContactList = null;
        try {
            userContactList = userContactMapper.friendList(friendListVO);
        } catch (Exception e) {
            e.printStackTrace();
            return APIUtil.logicErrorResponse(e.getLocalizedMessage(), e);
        }

        return APIUtil.getResponse(SUCCESS, userContactList);
    }

    /**
     * 来往记录
     */
    public APIResponse getContactInfo(APIRequest request) {
        UserContactParamVO userContactParamVO = (UserContactParamVO) request.getRequestParam();
        Integer user_id = TokenUtils.getInstance().getUserId();
        // handle param
        if (userContactParamVO.getAccess_token() == null || userContactParamVO.getAccess_token().length() == 0) {
            //传入公共token
            userContactParamVO.setAccess_token(SFTokenHelper.COMMON_ACCESSTOKEN);
        } else if (user_id == 0) {
            return APIUtil.paramErrorResponse("用户id不能为空");
        } else if (userContactParamVO.getFriend_id() == 0) {
            return APIUtil.paramErrorResponse("好友id不能为空");
        } else if (user_id == userContactParamVO.getFriend_id()) {
            return APIUtil.paramErrorResponse("I believe that you can't fuck yourself !");
        } else if (userContactParamVO.getPageNum() < 1 || userContactParamVO.getPageSize() < 1) {
            return APIUtil.paramErrorResponse("分页参数无效");
        }
        userContactParamVO.setUser_id(user_id);
        APIResponse apiResponse = syncFriendExpress(userContactParamVO);
        if (apiResponse != null) return apiResponse;


        //修改为物理分页参数
        userContactParamVO.setPageNum((userContactParamVO.getPageNum() - 1) * userContactParamVO.getPageSize()); // pageNum -> startIndex
        // callback
        List<FriendRecordDTO> friendRecordDTOS = userContactMapper.selectCirclesContact(userContactParamVO);
        for (FriendRecordDTO friendRecordDTO : friendRecordDTOS) {
            User sender = userMapper.selectUserByUserId(friendRecordDTO.getSender_user_id());
            User receiver = userMapper.selectUserByUserId(friendRecordDTO.getShip_user_id());
            if (sender != null) {
                friendRecordDTO.setSender_icon(sender.getAvatar());
                friendRecordDTO.setSender_wechatname(sender.getName());
            }
            if (receiver != null) {
                friendRecordDTO.setShip_icon(receiver.getAvatar());
                friendRecordDTO.setShip_wechatname(receiver.getName());
            }
        }
        return APIUtil.getResponse(SUCCESS, friendRecordDTOS);
    }

    private APIResponse syncFriendExpress(UserContactParamVO userContactParamVO) {
        // handle url
        String ORDERS_URL = SF_ORDER_SYNC_URL;
        String uuids = "";


//        List<OrderExpressDTO> orderExpressList = orderExpressMapper.selectExpressForId(userContactParamVO.getUser_id());
        PageHelper.startPage(userContactParamVO.getPageNum(), userContactParamVO.getPageSize());
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForContactInfo(userContactParamVO.getUser_id(), userContactParamVO.getFriend_id());


        for (OrderExpress oe : orderExpressList) { //此处订单若过多 url过长 sf接口会报414
            Order order = orderMapper.selectOrderDetailByOrderId(oe.getOrder_id());
            if (order != null && oe.getOrder_number() != null && !oe.getOrder_number().equals("")) {
                // 只有同城的订单能同步快递状态
                uuids = uuids + oe.getUuid() + ",";
            }
            if (oe.getOrder_number() == null || oe.getOrder_number().equals("")) {
                orderExpressMapper.updatePayState("WAIT_PAY", oe.getUuid());
            }
        }

        if (uuids.equals("")) return null; //无需同步的订单 直接返回
        uuids = uuids.substring(0, uuids.length() - 1);
        ORDERS_URL = ORDERS_URL.replace("{uuid}", uuids);

        // POST
        List<OrderSynVO> orderSynVOS = null;
        try {
            String token = userContactParamVO.getAccess_token();
            if (token != null && !token.equals("")) {
                token = userContactParamVO.getAccess_token();
            } else {
                token = COMMON_ACCESSTOKEN;
            }
            orderSynVOS = APIResolve.getOrderStatusWithUrl(ORDERS_URL, token);
        } catch (Exception e) {
            return APIUtil.submitErrorResponse("正在同步来往记录订单状态，稍后重试", e);
        }

        // update express status
        if (orderSynVOS != null) {
            for (OrderSynVO orderSynVO : orderSynVOS) {
                String uuid = orderSynVO.getUuid();
                Order order = orderMapper.selectOrderDetailByUuid(orderSynVO.getUuid());
                String order_status = (orderSynVO.isPayed() && orderSynVO.getStatus().equals("PAYING") &&
                        order.getPay_method().equals("FREIGHT_PREPAID")) ? "WAIT_HAND_OVER" : orderSynVO.getStatus();
                if (order.getPay_method().equals("FREIGHT_COLLECT")) { //到付订单的状态处理
                    order_status = (orderSynVO.getStatus().equals("PAYING") || orderSynVO.getStatus().equals("INIT")) ? "DELIVERING" : orderSynVO.getStatus();
                }
                String pay_state = orderSynVO.isPayed() ? "ALREADY_PAY" : "WAIT_PAY";
                //存在锁的问题，修改语句改为一条
                JSONObject attributesOBJ = orderSynVO.getAttributes();
                if (attributesOBJ.containsKey("abnormal_option")) {
                    String abNormalError = attributesOBJ.getString("abnormal_option");
                    if (abNormalError != null && abNormalError.equals("CUSTOMER_CANCEL") ||
                            abNormalError.equals("CONTACT_CUSTOMER_FAILURE") || abNormalError.equals("ERROR_CUSTOMER_ADDRESS") ||
                            abNormalError.equals("CONFORM_TO_ORDER_FAILURE") || abNormalError.equals("PICK_UP_OTHERS") ||
                            abNormalError.equals("DISPATCH_TIME_OUT")) {
                        order_status = "CANCELED";
                    } else if (abNormalError != null && abNormalError.equals("CUSTOMER_REJECTION") ||
                            abNormalError.equals("CONTACT_COURIER_FAILURE") || abNormalError.equals("CONTACT_RECEIVER_FAILURE") ||
                            abNormalError.equals("ERROR_RECEIVER_ADDRESS") || abNormalError.equals("TO_DROP_OFF_OTHERS") ||
                            abNormalError.equals("PAY_FAILURE") || abNormalError.equals("VERIFY_FAILURE")) {
                        order_status = "DELIVERING";
                    } else if (abNormalError != null && abNormalError.equals("UNABLE_TO_PICK_UP") ||
                            abNormalError.equals("DISPATCH_FAILED") || abNormalError.equals("DISCARD_TRIP_GROUP")) {
                        order_status = "WAIT_HAND_OVER";
                    }
                }
                String attributes = attributesOBJ.toString();
                orderExpressMapper.updateAttributesAndStatusByUUID(uuid, attributes, order_status, pay_state);
            }
        }
        return null;
    }

    /**
     * 星标好友
     */
    public APIResponse starFriend(APIRequest request) {
        // Param
        Object param = request.getRequestParam();
        JSONObject requestObject = JSONObject.fromObject(param);
        Integer user_id = TokenUtils.getInstance().getUserId();
        int friend_id = requestObject.getInt("friend_id");
        int is_star = requestObject.getInt("is_star");
        if (user_id < 1)
            return APIUtil.paramErrorResponse("参数user_id无效");
        if (friend_id < 1)
            return APIUtil.paramErrorResponse("参数friend_id无效");
        if (is_star != 0 && is_star != 1)
            return APIUtil.paramErrorResponse("参数is_star无效");

        UserContact userContact = userContactMapper.friendDetail(user_id, friend_id);
        if (userContact == null)
            return APIUtil.submitErrorResponse("非好友关系", null);

        // Result
        try {
            userContactMapper.starFriend(user_id, friend_id, is_star);
            userContact = userContactMapper.friendDetail(user_id, friend_id);
        } catch (Exception e) {
            return APIUtil.logicErrorResponse(e.getLocalizedMessage(), e);
        }

        return APIUtil.getResponse(SUCCESS, userContact);
    }

    /**
     * 更新 好友关系备注与图片
     */
    public APIResponse updateNotesAndPicture(APIRequest apiRequest) {
        Object requestParam = apiRequest.getRequestParam();
        JSONObject jsonObject = JSONObject.fromObject(requestParam);
        int user_contact_id = jsonObject.getInt("user_contact_id");
        String notes = jsonObject.getString("notes");
        String picture_address = jsonObject.getString("picture_address");
        String mobile = jsonObject.getString("mobile");
        userContactMapper.updateNotesPictureMobile(user_contact_id, notes, picture_address, mobile);
        String resultStr = user_contact_id + " notes:" + notes + "mobile:" + mobile + "picture：" + picture_address;
        return APIUtil.getResponse(SUCCESS, resultStr);
    }

    /**
     * CMS 获取好友列表 分页+条件
     */
    public APIResponse selectUserContactListByPage(APIRequest request) {

        HttpServletRequest httpServletRequest = request.getRequest();
        // 此处封装了 UserContact的构造方法
        UserContactNew userContactNew = new UserContactNew(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
        List<UserContactNew> userContactNewList = userContactMapper.selectByPage(userContactNew);
        if (userContactNewList.size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(SUCCESS, userContactNewList);
        }
    }
}
