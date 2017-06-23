package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;
import com.sftc.web.model.apiCallback.ContactCallback;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.UserContactParam;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.sfmodel.Orders;
import com.sftc.web.service.UserContactService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 联系人操作接口实现
 * @date 17/4/1
 * @Time 下午9:33
 */
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

    /*
     * 添加好友
     */
    public APIResponse addFriend(UserContactParam userContactParam) {
        APIStatus status = APIStatus.SUCCESS;
        userContactParam.setCreate_time(Long.toString(System.currentTimeMillis()));
        try {
            userContactMapper.addFriend(userContactParam);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    /*
     * 根据id查询好友详情
     */
    public APIResponse getFriendDetail(UserContactParam userContactParam) {
        APIStatus status = APIStatus.SUCCESS;
        int id = userContactParam.getId();
        UserContact userContact = userContactMapper.friendDetail(id);
        List<UserContactLabel> userContactLabelList = userContactLabelMapper.getFriendLabelList(id);
        List<DateRemind> dateRemindList = dateRemindMapper.getDateRemindList(id);
        userContact.setUserContactLabelList(userContactLabelList);
        userContact.setDateRemindList(dateRemindList);
        return APIUtil.getResponse(status, userContact);
    }

    /*
     * 获取某个用户的所有好友（带分页）
     */
    public APIResponse getFriendList(Paging paging) {
        APIStatus status = APIStatus.SUCCESS;
        paging.setPageNum(paging.getPageNum() - 1);
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
     * 好友圈来往记录
     */
    public APIResponse getContactInfo(UserContactParam userContactParam) {
        APIStatus status = APIStatus.SUCCESS;
        String ORDERS_URL = "http://api-dev.sf-rush.com/requests/uuid/status?batch=true";
        String uuids = "";
        List<OrderCallback> orderCallbacks = new ArrayList<OrderCallback>();
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForId(userContactParam.getUser_id());
        List<ContactCallback> contactCallbacks = null;
        for (OrderExpress oe : orderExpressList) {
            uuids = uuids + oe.getUuid() + ",";
        }
        uuids = uuids.substring(0, uuids.length() - 1);
        ORDERS_URL = ORDERS_URL.replace("uuid", uuids);
        try {
            List<Orders> orderses = APIResolve.getOrdersJson(ORDERS_URL, userContactParam.getToken());
            for (Orders orders : orderses) {
                if (!orders.getStatus().equals("WAIT_FILL") || !orders.getStatus().equals("ALREADY_FILL")) {
                    String uuid = orders.getUuid();
                    String order_status = orders.getStatus();
                    orderExpressMapper.updateOrderExpressForSF(new OrderExpress(order_status, uuid));
                }
            }
            contactCallbacks = userContactMapper.selectCirclesContact(userContactParam);
            for (ContactCallback contactCallback : contactCallbacks) {
                String sender_icon = userContactMapper.selectUserIcon(contactCallback.getSender_user_id());
                contactCallback.setSender_icon(sender_icon);
                String ship_icon = userContactMapper.selectUserIcon(contactCallback.getShip_user_id());
                contactCallback.setShip_icon(ship_icon);
            }
        } catch (Exception e) {
            status = APIStatus.SELECT_FAIL;
            e.printStackTrace();
        }
        return APIUtil.getResponse(status, contactCallbacks);
    }
}
