package com.sftc.web.model.dto;

import com.sftc.web.model.*;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.reqeustParam.OrderParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class OrderDTO extends Order {

    // 贺卡
    private GiftCard giftCard;
    // 订单评价信息
    private Evaluate evaluate;
    // 快递数组
//    private List<OrderExpress> orderExpressList;

    public List<OrderExpressDTO> orderExpressList;

    public Evaluate getEvaluate() {return evaluate;}

    public void setEvaluate(Evaluate evaluate) {this.evaluate = evaluate;}

    public List<OrderExpressDTO> getOrderExpressList() {return orderExpressList;}

    public void setOrderExpressList(List<OrderExpressDTO> orderExpressList) {this.orderExpressList = orderExpressList;}

//    public List<OrderExpressDTO> getOrderExpressDTOList() {
//        return orderExpressDTOList;
//    }

//    public void setOrderExpressDTOList(List<OrderExpressDTO> orderExpressDTOList) {
//        this.orderExpressDTOList = orderExpressDTOList;
//    }

    public OrderDTO() {}

    public OrderDTO(double longitude, double latitude, String id) {
        this.setId(id);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
    }

    public OrderDTO(String create_time, String pay_method,
                    String distribution_method, String sender_name, String sender_mobile, String sender_province,
                    String sender_city, String sender_area, String sender_addr, String supplementary_info,
                    double longitude, double latitude, String order_type, int sender_user_id) {
        this.setCreate_time(create_time);
        this.setPay_method(pay_method);
        this.setDistribution_method(distribution_method);
        this.setSender_name(sender_name);
        this.setSender_mobile(sender_mobile);
        this.setSender_province(sender_province);
        this.setSender_city(sender_city);
        this.setSender_area(sender_area);
        this.setSender_addr(sender_addr);
        this.setSupplementary_info(supplementary_info);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setOrder_type(order_type);
        this.setSender_user_id(sender_user_id);
    }
    public OrderDTO(String create_time, String pay_method,
                    String distribution_method, String sender_name, String sender_mobile, String sender_province,
                    String sender_city, String sender_area, String sender_addr,
                    double longitude, double latitude, String order_type, int sender_user_id) {
        this.setCreate_time(create_time);
        this.setPay_method(pay_method);
        this.setDistribution_method(distribution_method);
        this.setSender_name(sender_name);
        this.setSender_mobile(sender_mobile);
        this.setSender_province(sender_province);
        this.setSender_city(sender_city);
        this.setSender_area(sender_area);
        this.setSender_addr(sender_addr);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setOrder_type(order_type);
        this.setSender_user_id(sender_user_id);
    }

    public OrderDTO(OrderParam orderParam) {
        this.setCreate_time(Long.toString(System.currentTimeMillis()));
        this.setPay_method(orderParam.getPay_method());
        this.setDistribution_method(orderParam.getDistribution_method());
        this.setSender_name(orderParam.getSender_name());
        this.setSender_mobile(orderParam.getSender_mobile());
        this.setSender_province(orderParam.getSender_province());
        this.setSender_city(orderParam.getSender_city());
        this.setSender_area(orderParam.getSender_area());
        this.setSender_addr(orderParam.getSender_addr());
        this.setSupplementary_info(orderParam.getSupplementary_info());
        this.setWord_message(orderParam.getWord_message());
        this.setImage(orderParam.getImage());
        this.setVoice(orderParam.getVoice());
        this.setLongitude(orderParam.getLongitude());
        this.setLatitude(orderParam.getLatitude());
        this.setVoice_time(orderParam.getVoice_time());
        this.setSender_user_id(orderParam.getSender_user_id());
        this.setGift_card_id(orderParam.getGift_card_id());
        this.setOrder_type(orderParam.getOrder_type());
        this.setRegion_type(orderParam.getRegion_type());
    }

    /**
     * 基于HttpServletRequest作为参数的构造方法 用于cms
     * 后期便于应用扩展工厂模式 将此参数抽出
     */
    public OrderDTO(HttpServletRequest request) {
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
            this.setId(request.getParameter("id"));
        }
        if (request.getParameter("order_type") != null && !"".equals(request.getParameter("order_type"))) {
            this.setOrder_type(request.getParameter("order_type"));
        }
        if (request.getParameter("region_type") != null && !"".equals(request.getParameter("region_type"))) {
            this.setRegion_type(request.getParameter("region_type") );
        }
        if (request.getParameter("sender_mobile") != null && !"".equals(request.getParameter("sender_mobile"))) {
            this.setSender_mobile(request.getParameter("sender_mobile") );
        }
    }

    public GiftCard getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(GiftCard giftCard) {
        this.giftCard = giftCard;
    }


}
