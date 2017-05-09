package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.Order;
import com.sftc.web.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 订单操作接口实现
 * @date 17/4/1
 * @Time 下午9:34
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    public APIResponse placeOrder(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String order_state = (String) request.getParameter("state");
        String gmt_order_create = Long.toString(System.currentTimeMillis());
        String gmt_pay_create = Long.toString(System.currentTimeMillis());
        String pay_method = (String) request.getParameter("pay_method");
        double freight = Double.parseDouble((String) request.getParameter("freight"));
        String sender_name = (String) request.getParameter("sender_name");
        String sender_mobile = (String) request.getParameter("sender_mobile");
        String sender_province = (String) request.getParameter("sender_province");
        String sender_city = (String) request.getParameter("sender_city");
        String sender_area = (String) request.getParameter("sender_area");
        String sender_addr = (String) request.getParameter("sender_addr");
        String ship_name = (String) request.getParameter("ship_name");
        String ship_mobile = (String) request.getParameter("ship_mobile");
        String ship_province = (String) request.getParameter("ship_province");
        String ship_city = (String) request.getParameter("ship_city");
        String ship_area = (String) request.getParameter("ship_area");
        String ship_addr = (String) request.getParameter("ship_addr");
        String memos = (String) request.getParameter("memos");
        String type = (String) request.getParameter("type");
        String size = (String) request.getParameter("size");
        int user_id = Integer.parseInt((String) request.getParameter("user_id"));
        String images = (String) request.getParameter("images");
        String voice = (String) request.getParameter("voice");
        String create_time = Long.toString(System.currentTimeMillis());
        int gift_card_id = Integer.parseInt((String) request.getParameter("gift_card_id"));

        Order order = new Order(order_state, gmt_order_create, gmt_pay_create, pay_method, freight,
                sender_name, sender_mobile, sender_province, sender_city, sender_area,
                sender_addr, ship_name, ship_mobile, ship_province, ship_city, ship_area,
                ship_addr, memos, type, size, user_id, images, voice, create_time, gift_card_id);
        try {
            orderMapper.addOrder(order);
        } catch (Exception e) {

            status = APIStatus.ORDER_FAIL;
            e.printStackTrace();

        }
        return APIUtil.getResponse(status, null);
    }
}
