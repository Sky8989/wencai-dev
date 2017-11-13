package com.sftc.web.model.dto;

import com.sftc.web.model.entity.OrderExpress;

import java.util.List;

public class FriendRecordDTO {

    private int id;

    // 寄件人姓名
    private String sender_name;
    // 寄件人的用户id
    private int sender_user_id;
    // 寄件人的头像
    private String sender_icon;

    // 收件人的姓名
    private String ship_name;
    // 收件人的用户id
    private int ship_user_id;
    // 收件人的头像
    private String ship_icon;

    // 物品类型
    private String object_type;
    // 订单编号
    private String order_id;
    // 快递状态
    private String state;
    // 礼物卡id
    private int gift_card_id;

    // 创建时间
    private String create_time;

    private String region_type;

    // 寄件人微信名
    private String sender_wechatname;
    // 收件人微信名
    private String ship_wechatname;
    //为C端小程序的物品类型，
    private String package_type;
    //支付方式
    private String pay_method;

    public String getRegion_type() {
        return region_type;
    }

    public void setRegion_type(String region_type) {
        this.region_type = region_type;
    }

    //当物品类型在packageType罗列的类型之外时填写
    private String package_comments;
    //快递uuid
    private String uuid;

    private List<OrderExpress> orderExpressList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public int getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(int sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public String getSender_icon() {
        return sender_icon;
    }

    public void setSender_icon(String sender_icon) {
        this.sender_icon = sender_icon;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public int getShip_user_id() {
        return ship_user_id;
    }

    public void setShip_user_id(int ship_user_id) {
        this.ship_user_id = ship_user_id;
    }

    public String getShip_icon() {
        return ship_icon;
    }

    public void setShip_icon(String ship_icon) {
        this.ship_icon = ship_icon;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getGift_card_id() {
        return gift_card_id;
    }

    public void setGift_card_id(int gift_card_id) {
        this.gift_card_id = gift_card_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSender_wechatname() {
        return sender_wechatname;
    }

    public void setSender_wechatname(String sender_wechatname) {
        this.sender_wechatname = sender_wechatname;
    }

    public String getShip_wechatname() {
        return ship_wechatname;
    }

    public void setShip_wechatname(String ship_wechatname) {
        this.ship_wechatname = ship_wechatname;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getPackage_comments() {
        return package_comments;
    }

    public void setPackage_comments(String package_comments) {
        this.package_comments = package_comments;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<OrderExpress> getOrderExpressList() {return orderExpressList;}

    public void setOrderExpressList(List<OrderExpress> orderExpressList) {this.orderExpressList = orderExpressList;}

    public String getPay_method() {return pay_method;}

    public void setPay_method(String pay_method) {this.pay_method = pay_method;}
}
