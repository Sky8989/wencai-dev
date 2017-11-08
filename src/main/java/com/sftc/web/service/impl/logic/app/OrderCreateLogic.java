package com.sftc.web.service.impl.logic.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.common.EmojiFilter;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.web.dao.jpa.OrderDao;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.mybatis.*;
import com.sftc.web.model.entity.Message;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.UserContactNew;
import com.sftc.web.model.reqeustParam.OrderParam;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Transactional
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
    @Resource
    private OrderCommitLogic orderCommitLogic;
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderExpressDao orderExpressDao;

    /**
     * 寄件人填写订单
     */
    public APIResponse friendPlaceOrder(APIRequest request) {

        OrderParam orderParam = (OrderParam) request.getRequestParam();

        // 增加对emoji的过滤
        if (orderParam.getPackage_type() != null && !"".equals(orderParam.getPackage_type())) {
            boolean containsEmoji = EmojiFilter.containsEmoji(orderParam.getPackage_type());
            if (containsEmoji) return APIUtil.paramErrorResponse("Don't input emoji");
        }
        if (orderParam.getObject_type() != null && !"".equals(orderParam.getObject_type())) {
            boolean containsEmoji = EmojiFilter.containsEmoji(orderParam.getObject_type());
            if (containsEmoji) return APIUtil.paramErrorResponse("Don't input emoji");
        }
        if (orderParam.getPackage_comments() != null && !"".equals(orderParam.getPackage_comments())) {
            boolean containsEmoji = EmojiFilter.containsEmoji(orderParam.getPackage_comments());
            if (containsEmoji) return APIUtil.paramErrorResponse("Don't input emoji");
        }
        // 处理package_comments 非必填参数
        String package_comments = orderParam.getPackage_comments() != null ? orderParam.getPackage_comments() : "";

        // 插入订单表
        Order order = new Order(orderParam);
        orderDao.save(order);

        // 插入快递表
        for (int i = 0; i < orderParam.getPackage_count(); i++) {
            // 写入uuid 保证每个快递的uuid不同
            OrderExpress orderExpress = new OrderExpress();
            orderExpress.setPackage_type(orderParam.getPackage_type());
            orderExpress.setObject_type(orderParam.getObject_type());
            orderExpress.setOrder_id(order.getId());
            orderExpress.setCreate_time(order.getCreate_time());
            orderExpress.setState("WAIT_FILL");
            orderExpress.setSender_user_id(orderParam.getSender_user_id());
            orderExpress.setReserve_time("");
            orderExpress.setOrder_id(order.getId());
            orderExpress.setPackage_comments(package_comments);
            orderExpress.setUuid(SFOrderHelper.getOrderNumber());
            orderExpressDao.save(orderExpress);//TODO:要插入包裹补充信息
        }

        // 插入地址表
        //com.sftc.web.model.AddressDTO senderAddress = new com.sftc.web.model.AddressDTO(orderParam);
        //addressMapper.addAddress(senderAddress);

        return APIUtil.getResponse(SUCCESS, order.getId());
    }

    /**
     * 好友填写寄件订单
     */
    //使用最高级别的事物 防止提交过程中有好友包裹被填写
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public synchronized APIResponse friendFillOrder(Map rowData) {
        JSONObject paramOBJ = JSONObject.fromObject(rowData);
//        // 修复 空格对Gson的影响
//        String strJsonResult = orderExpressStr.replace(" ", "");
//        OrderExpressDTO orderExpress = new Gson().fromJson(strJsonResult, OrderExpressDTO.class);
        OrderExpress orderExpress = (OrderExpress) JSONObject.toBean(paramOBJ, OrderExpress.class);
        // 判断订单是否下单
        OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(orderExpress.getOrder_id());
        if (orderDTO.getRegion_type() != null && !"".equals(orderDTO.getRegion_type()) && orderDTO.getRegion_type().length() != 0) {
            return APIUtil.submitErrorResponse("订单已经下单，现在您无法再填写信息", orderExpress.getOrder_id());
        }

        if (orderDTO.getIs_cancel() == 1) {
            return APIUtil.submitErrorResponse("订单已取消，现在您无法再填写信息", null);
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
            return APIUtil.submitErrorResponse("包裹已经分发完", null);
        } else {
            //增加对supplementary_info的处理 保证有值
            OrderExpress orderExpress1 = orderExpressDao.findOne(realList.get(0).getId());
            if (orderExpress1.getSupplementary_info() == null || "".equals(orderExpress.getSupplementary_info())) {
                orderExpress1.setSupplementary_info(" ");
            }
            orderExpress1.setShip_user_id(orderExpress.getShip_user_id());
            orderExpress1.setShip_name(orderExpress.getShip_name());
            orderExpress1.setShip_mobile(orderExpress.getShip_mobile());
            orderExpress1.setShip_province(orderExpress.getShip_province());
            orderExpress1.setShip_city(orderExpress.getShip_city());
            orderExpress1.setShip_area(orderExpress.getShip_area());
            orderExpress1.setShip_addr(orderExpress.getShip_addr());
            orderExpress1.setSupplementary_info(orderExpress.getSupplementary_info());
            orderExpress1.setLatitude(orderExpress.getLatitude());
            orderExpress1.setLongitude(orderExpress.getLongitude());
            orderExpress1.setOrder_id(orderExpress.getOrder_id());
            orderExpress1.setState("ALREADY_FILL");
            orderExpress1.setId(realList.get(0).getId());
            orderExpress1.setReceive_time(Long.toString(System.currentTimeMillis()));
            orderExpressDao.save(orderExpress1);

            // 添加RECEIVE_ADDRESS通知消息，此时应该是寄件人收到通知
            List<Message> messageList = messageMapper.selectMessageReceiveAddress(orderDTO.getSender_user_id());
            if (messageList.isEmpty() && messageList.size() == 0) {
                Message message = new Message("RECEIVE_ADDRESS", 0, realList.get(0).getId(), orderDTO.getSender_user_id());
                messageMapper.insertMessage(message);
            } else {
                Message message = messageList.get(0);
                message.setIs_read(0);
                message.setExpress_id(realList.get(0).getId());
                message.setUser_id(orderDTO.getSender_user_id());
                message.setCreate_time(Long.toString(System.currentTimeMillis()));
                messageMapper.updateMessageReceiveAddress(message);
            }
            // 消息通知表插入或者更新消息
            List<Message> messageListRE = messageMapper.selectMessageReceiveExpress(orderExpress1.getShip_user_id());
            if (messageListRE.isEmpty()&& messageListRE.size() == 0) {
                Message message = new Message("RECEIVE_EXPRESS", 0, realList.get(0).getId(), orderExpress1.getShip_user_id());
                messageMapper.insertMessage(message);
            } else {
                Message message = messageListRE.get(0);
                message.setIs_read(0);
                message.setExpress_id(realList.get(0).getId());
                message.setUser_id(orderExpress1.getShip_user_id());
                message.setCreate_time(Long.toString(System.currentTimeMillis()));
                messageMapper.updateMessageReceiveExpress(message);
            }
            // 存储好友关系 寄件人对填写人的好友关系
            UserContactNew userContactNewParam = new UserContactNew();
            userContactNewParam.setUser_id(orderDTO.getSender_user_id());             // 寄件人
            userContactNewParam.setFriend_id(orderExpress1.getShip_user_id());      // 填写人
            UserContactNew userContactNew = userContactMapper.selectByUserIdAndShipId(userContactNewParam);
            if (userContactNew == null) { // null即 还不是好友关系
                userContactNew = new UserContactNew();
                userContactNew.setUser_id(orderDTO.getSender_user_id());
                userContactNew.setFriend_id(orderExpress1.getShip_user_id());
                userContactNew.setIs_tag_star(0);
                userContactNew.setLntimacy(1);
                userContactNew.setCreate_time(Long.toString(System.currentTimeMillis()));
                userContactMapper.insertUserContact(userContactNew);
            } else {// 如果不为空 则好友度加1
                userContactMapper.updateUserContactLntimacy(userContactNew);
            }

            // 存储好友关系   填写人对寄件人的好友关系
            userContactNewParam.setUser_id(orderExpress1.getShip_user_id()); // 填写人
            userContactNewParam.setFriend_id(orderDTO.getSender_user_id());    // 寄件人
            UserContactNew userContactNew2 = userContactMapper.selectByUserIdAndShipId(userContactNewParam);
            if (userContactNew2 == null) {
                userContactNew2 = new UserContactNew();
                userContactNew2.setUser_id(orderExpress1.getShip_user_id());
                userContactNew2.setFriend_id(orderDTO.getSender_user_id());
                userContactNew2.setIs_tag_star(0);
                userContactNew2.setLntimacy(1);
                userContactNew2.setCreate_time(Long.toString(System.currentTimeMillis()));
                userContactMapper.insertUserContact(userContactNew2);
            } else {// 如果不为空 则好友度加1
                userContactMapper.updateUserContactLntimacy(userContactNew2);
            }

            String current_create_time = Long.toString(System.currentTimeMillis());
            ///插入地址映射关系 1 地址簿 1 历史地址
            // 生成 收件人的寄件人地址簿
            orderCommitLogic.insertAddressBookUtils("address_book", "sender",
                    orderExpress1.getShip_user_id(),//给收件人存
                    orderExpress1.getShip_user_id(),//给收件人存
                    orderExpress1.getShip_name(), orderExpress1.getShip_mobile(), orderExpress1.getShip_province(),
                    orderExpress1.getShip_city(), orderExpress1.getShip_area(), orderExpress1.getShip_addr(), orderExpress1.getSupplementary_info(),
                    current_create_time, orderExpress1.getLongitude(), orderExpress1.getLatitude()
            );

            //提交地址同时（去重处理）保存到[寄件人]最近联系人
            orderCommitLogic.insertAddressBookUtils("address_history", "address_history",
                    orderDTO.getSender_user_id(),// 给寄件人存
                    orderExpress1.getShip_user_id(),// id是收件人的 查历史地址时才能取到
                    orderExpress1.getShip_name(), orderExpress1.getShip_mobile(), orderExpress1.getShip_province(),
                    orderExpress1.getShip_city(), orderExpress1.getShip_area(), orderExpress1.getShip_addr(), orderExpress1.getSupplementary_info(),
                    current_create_time, orderExpress1.getLongitude(), orderExpress1.getLatitude()
            );
        }

        return APIUtil.getResponse(SUCCESS, orderExpress.getOrder_id());
    }
}
