package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;
import com.sftc.web.model.apiCallback.ContactCallback;
import com.sftc.web.model.reqeustParam.UserContactParam;
import com.sftc.web.model.sfmodel.Orders;
import com.sftc.web.service.UserContactService;
import net.sf.json.JSONObject;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.Object;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Service("userContactService")
public class UserContactServiceImpl implements UserContactService {

    @Resource
    private UserContactMapper userContactMapper;

    @Resource
    private UserContactLabelMapper userContactLabelMapper;

    @Resource
    private DateRemindMapper dateRemindMapper;

    @Resource
    private OrderExpressMapper orderExpressMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

//    /*
//     * 添加好友
//     */
//    public APIResponse addFriend(UserContactParam userContactParam) {
//        APIStatus status = APIStatus.SUCCESS;
//        userContactParam.setCreate_time(Long.toString(System.currentTimeMillis()));
//        try {
//            userContactMapper.addFriend(userContactParam);
//        } catch (Exception e) {
//            e.printStackTrace();
//            status = APIStatus.SUBMIT_FAIL;
//        }
//        return APIUtil.getResponse(status, null);
//    }

    /*
     * 根据id查询好友详情
     */
    public APIResponse getFriendDetail(APIRequest request) {

        // Param
        String userId = (String) request.getParameter("user_id");
        String friendId = (String) request.getParameter("friend_id");

        if (userId == null || userId.equals(""))
            return APIUtil.paramErrorResponse("user_id不能为空");
        if (friendId == null || friendId.equals(""))
            return APIUtil.paramErrorResponse("friend_id不能为空");

        int user_id = Integer.parseInt(userId);
        int friend_id = Integer.parseInt(friendId);

        if (user_id < 1)
            return APIUtil.paramErrorResponse("user_id无效");
        if (friend_id < 1)
            return APIUtil.paramErrorResponse("friend_id无效");

        UserContact userContact = userContactMapper.friendDetail(user_id, friend_id);

        if (userContact == null)
            return APIUtil.selectErrorResponse("非好友关系", null);

        List<UserContactLabel> userContactLabelList = userContactLabelMapper.getFriendLabelList(user_id);
        List<DateRemind> dateRemindList = dateRemindMapper.selectDateRemindList(user_id);
        userContact.setUserContactLabelList(userContactLabelList);
        userContact.setDateRemindList(dateRemindList);
        return APIUtil.getResponse(SUCCESS, userContact);
    }

    /**
     * 获取用户的好友列表
     */
    public APIResponse getFriendList(APIRequest request) {
        Paging paging = (Paging) request.getRequestParam();

        // verify params
        if (paging.getUser_id() == 0) {
            return APIUtil.paramErrorResponse("用户id不能为空");
        } else if (paging.getPageNum() < 1 || paging.getPageSize() < 1) {
            return APIUtil.paramErrorResponse("分页参数无效");
        }

        APIStatus status = SUCCESS;
        paging.setPageNum((paging.getPageNum() - 1) * paging.getPageSize());
        List<UserContact> userContactList = null;
        try {
            userContactList = userContactMapper.friendList(paging);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SELECT_FAIL;
        }

        return APIUtil.getResponse(status, userContactList);
    }

    /**
     * 来往记录
     */
    public APIResponse getContactInfo(UserContactParam userContactParam) {

        APIStatus status = SUCCESS;

        // handle param
        if (userContactParam.getAccess_token() == null || userContactParam.getAccess_token().length() == 0) {
            return APIUtil.paramErrorResponse("access_token不能为空");
        } else if (userContactParam.getUser_id() == 0) {
            return APIUtil.paramErrorResponse("用户id不能为空");
        } else if (userContactParam.getFriend_id() == 0) {
            return APIUtil.paramErrorResponse("好友id不能为空");
        } else if (userContactParam.getUser_id() == userContactParam.getFriend_id()) {
            return APIUtil.paramErrorResponse("I believe that you can't fuck yourself !");
        } else if (userContactParam.getPageNum() < 1 || userContactParam.getPageSize() < 1) {
            return APIUtil.paramErrorResponse("分页参数无效");
        }

        userContactParam.setPageNum((userContactParam.getPageNum() - 1) * userContactParam.getPageSize()); // pageNum -> startIndex

        // handle url
        String ORDERS_URL = "http://api-dev.sf-rush.com/requests/uuid/status?batch=true";
        String uuids = "";
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForId(userContactParam.getUser_id());
        for (OrderExpress oe : orderExpressList) {
            String uuid = oe.getUuid();
            if (uuid != null && uuid.length() > 1) {
                uuids = uuids + oe.getUuid() + ",";
            }
        }
        uuids = uuids.substring(0, uuids.length() - 1);
        ORDERS_URL = ORDERS_URL.replace("uuid", uuids);

        // POST
        List<Orders> orderses = null;
        try {
            orderses = APIResolve.getOrdersJson(ORDERS_URL, userContactParam.getAccess_token());
        } catch (Exception e) {
            status = APIStatus.SELECT_FAIL;
            e.printStackTrace();
        }

        // update express status
        if (orderses != null) {
            for (Orders orders : orderses) {
                String uuid = orders.getUuid();
                String order_status = orders.getStatus();
                orderExpressMapper.updateOrderExpressForSF(new OrderExpress(order_status, uuid));
            }
        }

        // callback
        List<ContactCallback> contactCallbacks = userContactMapper.selectCirclesContact(userContactParam);
        for (ContactCallback contactCallback : contactCallbacks) {
            User sender = userMapper.selectUserByUserId(contactCallback.getSender_user_id());
            User receiver = userMapper.selectUserByUserId(contactCallback.getShip_user_id());
            if (sender != null)
                contactCallback.setSender_icon(sender.getAvatar());
            if (receiver != null)
                contactCallback.setShip_icon(receiver.getAvatar());
        }

        return APIUtil.getResponse(status, contactCallbacks);
    }

    /**
     * 星标好友
     */
    public APIResponse starFriend(APIRequest request) {
        // Param
        Object param = request.getRequestParam();
        JSONObject requestObject = JSONObject.fromObject(param);
        int user_id = ((Double) requestObject.get("user_id")).intValue();
        int friend_id = ((Double) requestObject.get("friend_id")).intValue();
        int is_star = ((Double) requestObject.get("is_star")).intValue();
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
            return APIUtil.submitErrorResponse(e.getLocalizedMessage(), null);
        }

        return APIUtil.getResponse(SUCCESS, userContact);
    }

    /**
     * CMS 获取好友列表 分页+条件
     */
    public APIResponse selectUserContactListByPage(APIRequest request){
        APIStatus status = APIStatus.SUCCESS;
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
            return APIUtil.getResponse(status, userContactNewList);
        }
    }
}
