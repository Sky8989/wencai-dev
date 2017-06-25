package com.sftc.web.service.impl;

import com.sftc.tools.api.APIResolve;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;
import com.sftc.web.model.apiCallback.ContactCallback;
import com.sftc.web.model.apiCallback.OrderCallback;
import com.sftc.web.model.reqeustParam.UserContactParam;
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
//
//    /*
//     * 根据id查询好友详情
//     */
//    public APIResponse getFriendDetail(UserContactParam userContactParam) {
//        APIStatus status = APIStatus.SUCCESS;
//        int id = userContactParam.getId();
//        UserContact userContact = userContactMapper.friendDetail(id);
//        List<UserContactLabel> userContactLabelList = userContactLabelMapper.getFriendLabelList(id);
//        List<DateRemind> dateRemindList = dateRemindMapper.getDateRemindList(id);
//        userContact.setUserContactLabelList(userContactLabelList);
//        userContact.setDateRemindList(dateRemindList);
//        return APIUtil.getResponse(status, userContact);
//    }

//    /*
//     * 获取某个用户的所有好友（带分页）
//     */
//    public APIResponse getFriendList(Paging paging) {
//        APIStatus status = APIStatus.SUCCESS;
//        paging.setPageNum(paging.getPageNum() - 1);
//        List<UserContact> userContactList = null;
//        try {
//            userContactList = userContactMapper.friendList(paging);
//        } catch (Exception e) {
//            e.printStackTrace();
//            status = APIStatus.SELECT_FAIL;
//        }
//        return APIUtil.getResponse(status, userContactList);
//    }

    /**
     * 来往记录
     */
    public APIResponse getContactInfo(UserContactParam userContactParam) {

        APIStatus status = APIStatus.SUCCESS;

        // handle param
        if (userContactParam.getAccess_token() == null || userContactParam.getAccess_token().length() == 0) {
            return APIUtil.errorResponse("access_token不能为空");
        } else if (userContactParam.getUser_id() == 0) {
            return APIUtil.errorResponse("用户id不能为空");
        } else if (userContactParam.getFriend_id() == 0) {
            return APIUtil.errorResponse("好友id不能为空");
        } else if (userContactParam.getUser_id() == userContactParam.getFriend_id()) {
            return APIUtil.errorResponse("I believe that you can't fuck yourself !");
        } else if (userContactParam.getPageNum() < 1 || userContactParam.getPageSize() < 1) {
            return APIUtil.errorResponse("分页参数无效");
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
}
