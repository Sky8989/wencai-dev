package com.sftc.web.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 订单类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class Order {

    private int id ;
    // 订单编号
    private String order_number;
    // 订单状态
    private String state;
    // 下单时间
    private String gmt_order_create;
    // 支付时间
    private String gmt_pay_create;
    // 付款方式
    private String pay_method;
    // 运费
    private double freight;
    // 寄件人id(根据用户表id)
    private User user;
    private int user_id;
    // 寄件人姓名
    private String sender_name;
    // 寄件人手机
    private String sender_mobile;
    // 寄件人省
    private String sender_province;
    // 寄件人市
    private String sender_city;
    // 寄件人详细地址
    private String sender_area;
    // 寄件人详细地址
    private String sender_addr;
    // 收件人姓名
    private String ship_name;
    // 收件人手机
    private String ship_mobile;
    // 收件人省
    private String ship_province;
    // 收件人市
    private String ship_city;
    // 收件人区
    private String ship_area;
    // 收件人详细地址
    private String ship_addr;
    // 订单备注
    private String memos;
    // 包裹类型
    private String type;
    // 包裹大小
    private String size;

    public Order(String state, String gmt_order_create, String gmt_pay_create, String pay_method, double freight, String sender_name, String sender_mobile, String sender_province, String sender_city, String sender_area, String sender_addr, String ship_name, String ship_mobile, String ship_province, String ship_city, String ship_area, String ship_addr, String memos, String type, String size, int user_id) {
        this.state = state;
        this.gmt_order_create = gmt_order_create;
        this.gmt_pay_create = gmt_pay_create;
        this.pay_method = pay_method;
        this.freight = freight;
        this.sender_name = sender_name;
        this.sender_mobile = sender_mobile;
        this.sender_province = sender_province;
        this.sender_city = sender_city;
        this.sender_area = sender_area;
        this.sender_addr = sender_addr;
        this.ship_name = ship_name;
        this.ship_mobile = ship_mobile;
        this.ship_province = ship_province;
        this.ship_city = ship_city;
        this.ship_area = ship_area;
        this.ship_addr = ship_addr;
        this.memos = memos;
        this.type = type;
        this.size = size;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGmt_order_create() {
        return gmt_order_create;
    }

    public void setGmt_order_create(String gmt_order_create) {
        this.gmt_order_create = gmt_order_create;
    }

    public String getGmt_pay_create() {
        return gmt_pay_create;
    }

    public void setGmt_pay_create(String gmt_pay_create) {
        this.gmt_pay_create = gmt_pay_create;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_mobile() {
        return sender_mobile;
    }

    public void setSender_mobile(String sender_mobile) {
        this.sender_mobile = sender_mobile;
    }

    public String getSender_province() {
        return sender_province;
    }

    public void setSender_province(String sender_province) {
        this.sender_province = sender_province;
    }

    public String getSender_city() {
        return sender_city;
    }

    public void setSender_city(String sender_city) {
        this.sender_city = sender_city;
    }

    public String getSender_area() {
        return sender_area;
    }

    public void setSender_area(String sender_area) {
        this.sender_area = sender_area;
    }

    public String getSender_addr() {
        return sender_addr;
    }

    public void setSender_addr(String sender_addr) {
        this.sender_addr = sender_addr;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public String getShip_mobile() {
        return ship_mobile;
    }

    public void setShip_mobile(String ship_mobile) {
        this.ship_mobile = ship_mobile;
    }

    public String getShip_province() {
        return ship_province;
    }

    public void setShip_province(String ship_province) {
        this.ship_province = ship_province;
    }

    public String getShip_city() {
        return ship_city;
    }

    public void setShip_city(String ship_city) {
        this.ship_city = ship_city;
    }

    public String getShip_area() {
        return ship_area;
    }

    public void setShip_area(String ship_area) {
        this.ship_area = ship_area;
    }

    public String getShip_addr() {
        return ship_addr;
    }

    public void setShip_addr(String ship_addr) {
        this.ship_addr = ship_addr;
    }

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
