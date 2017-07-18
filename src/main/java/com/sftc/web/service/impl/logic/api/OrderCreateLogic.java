package com.sftc.web.service.impl.logic.api;

import com.google.gson.Gson;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.web.mapper.*;
import com.sftc.web.model.Message;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.UserContactNew;
import com.sftc.web.model.reqeustParam.OrderParam;
import com.sftc.web.service.QiniuService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Component
public class OrderCreateLogic {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private UserContactMapper userContactMapper;
    @Resource
    private MessageMapper messageMapper;

    /**
     * 寄件人填写订单
     */
    public APIResponse friendPlaceOrder(APIRequest request) {

        OrderParam orderParam = (OrderParam) request.getRequestParam();

        // 插入订单表
        Order order = new Order(orderParam);
        String randomNumber = SFOrderHelper.getOrderNumber();
        orderMapper.addOrder(order);

        // 插入快递表
        OrderExpress orderExpress = new OrderExpress();
        orderExpress.setPackage_type(orderParam.getPackage_type());
        orderExpress.setObject_type(orderParam.getObject_type());
        orderExpress.setOrder_id(order.getId());
        orderExpress.setCreate_time(order.getCreate_time());
        orderExpress.setState("WAIT_FILL");
        orderExpress.setSender_user_id(orderParam.getSender_user_id());
        orderExpress.setReserve_time("");
        orderExpress.setOrder_id(order.getId());
        for (int i = 0; i < orderParam.getPackage_count(); i++) {
            // 写入uuid 保证每个快递的uuid不同
            orderExpress.setUuid(SFOrderHelper.getOrderNumber());
            orderExpressMapper.addOrderExpress(orderExpress);
        }

        // 插入地址表
        com.sftc.web.model.Address senderAddress = new com.sftc.web.model.Address(orderParam);
        addressMapper.addAddress(senderAddress);

        return APIUtil.getResponse(SUCCESS, order.getId());
    }

    /**
     * 好友填写寄件订单
     */
    public synchronized APIResponse friendFillOrder(Map rowData) {

        String orderExpressStr = rowData.toString();
        // 修复 空格对Gson的影响
        String strJsonResult = orderExpressStr.replace(" ", "");
        OrderExpress orderExpress = new Gson().fromJson(strJsonResult, OrderExpress.class);
        // 判断订单是否下单
        Order order = orderMapper.selectOrderDetailByOrderId(orderExpress.getOrder_id());
        if (order.getRegion_type() != null && !"".equals(order.getRegion_type()) && order.getRegion_type().length() != 0) {
            return APIUtil.submitErrorResponse("订单已经下单，现在您无法再填写信息", orderExpress.getOrder_id());
        }

        // 判断同一个用户重复填写
        LinkedList<OrderExpress> realList = new LinkedList<OrderExpress>();
        List<OrderExpress> preList = orderExpressMapper.findAllOrderExpressByOrderId(orderExpress.getOrder_id());
        for (OrderExpress oe : preList) {
            if (oe.getShip_user_id() == orderExpress.getShip_user_id())
                return APIUtil.submitErrorResponse("您已经填写过信息，请勿重复填写", orderExpress.getOrder_id());
            if (oe.getShip_user_id() == 0)
                realList.add(oe);
        }

        if (realList.isEmpty()) { // 已抢完
            return APIUtil.getResponse(APIStatus.ORDER_PACKAGE_COUNT_PULL, null);
        } else {
            // 随机获取包裹编号
            int random = new Random().nextInt(realList.size());
            orderExpress.setState("ALREADY_FILL");
            //orderExpress.setId(realList.get(random).getId());
            // 修改需求为 不需要随机取包裹 6.30号
            orderExpress.setId(realList.get(0).getId());
            orderExpress.setReceive_time(Long.toString(System.currentTimeMillis()));
            orderExpressMapper.updateOrderExpressByOrderExpressId(orderExpress);

            // 添加RECEIVE_ADDRESS通知消息，此时应该是寄件人收到通知
            List<Message> messageList = messageMapper.selectMessageReceiveAddress(order.getSender_user_id());
            if (messageList.isEmpty() && messageList.size() == 0) {
                Message message = new Message("RECEIVE_ADDRESS", 0, orderExpress.getId(), order.getSender_user_id());
                messageMapper.insertMessage(message);
            } else {
                Message message = messageList.get(0);
                message.setIs_read(0);
                message.setExpress_id(orderExpress.getId());
                message.setUser_id(order.getSender_user_id());
                message.setCreate_time(Long.toString(System.currentTimeMillis()));
                messageMapper.updateMessageReceiveAddress(message);
            }
            // 消息通知表插入或者更新消息
            List<Message> messageListRE = messageMapper.selectMessageReceiveExpress(orderExpress.getShip_user_id());
            if (messageListRE.isEmpty()) {
                Message message = new Message("RECEIVE_EXPRESS", 0, orderExpress.getId(), orderExpress.getShip_user_id());
                messageMapper.insertMessage(message);
            } else {
                Message message = messageListRE.get(0);
                message.setIs_read(0);
                message.setExpress_id(orderExpress.getId());
                message.setCreate_time(Long.toString(System.currentTimeMillis()));
                messageMapper.updateMessageReceiveExpress(message);
            }
            // 存储好友关系 寄件人对填写人的好友关系
            UserContactNew userContactNewParam = new UserContactNew();
            userContactNewParam.setUser_id(order.getSender_user_id());             // 寄件人
            userContactNewParam.setFriend_id(orderExpress.getShip_user_id());      // 填写人
            UserContactNew userContactNew = userContactMapper.selectByUserIdAndShipId(userContactNewParam);
            if (userContactNew == null) { // null即 还不是好友关系
                userContactNew = new UserContactNew();
                userContactNew.setUser_id(order.getSender_user_id());
                userContactNew.setFriend_id(orderExpress.getShip_user_id());
                userContactNew.setIs_tag_star(0);
                userContactNew.setLntimacy(1);
                userContactNew.setCreate_time(Long.toString(System.currentTimeMillis()));
                userContactMapper.insertUserContact(userContactNew);
            } else {// 如果不为空 则好友度加1
                userContactMapper.updateUserContactLntimacy(userContactNew);
            }

            // 存储好友关系   填写人对寄件人的好友关系
            userContactNewParam.setUser_id(orderExpress.getShip_user_id()); // 填写人
            userContactNewParam.setFriend_id(order.getSender_user_id());    // 寄件人
            UserContactNew userContactNew2 = userContactMapper.selectByUserIdAndShipId(userContactNewParam);
            if (userContactNew2 == null) {
                userContactNew2 = new UserContactNew();
                userContactNew2.setUser_id(orderExpress.getShip_user_id());
                userContactNew2.setFriend_id(order.getSender_user_id());
                userContactNew2.setIs_tag_star(0);
                userContactNew2.setLntimacy(1);
                userContactNew2.setCreate_time(Long.toString(System.currentTimeMillis()));
                userContactMapper.insertUserContact(userContactNew2);
            } else {// 如果不为空 则好友度加1
                userContactMapper.updateUserContactLntimacy(userContactNew2);
            }

        }

        return APIUtil.getResponse(SUCCESS, orderExpress.getOrder_id());
    }
}
