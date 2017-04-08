package com.sftc.web.model;

import java.sql.Date;
import java.util.List;

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
    private Date order_time;
    // 支付时间
    private Date pay_time;
    // 付款方式
    private String payment_method;
    //运费
    private double commodity_price;
    // 寄件人id(根据用户表id)
    private User user;
    private int user_id;
    // 地址表id
    private Address address;
    private int sender_address_id;
    // 地址表id
    private int getter_address_id;
    private List<Commodity> commodityList;

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

    public Date getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Date order_time) {
        this.order_time = order_time;
    }

    public Date getPay_time() {
        return pay_time;
    }

    public void setPay_time(Date pay_time) {
        this.pay_time = pay_time;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public double getCommodity_price() {
        return commodity_price;
    }

    public void setCommodity_price(double commodity_price) {
        this.commodity_price = commodity_price;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getSender_address_id() {
        return sender_address_id;
    }

    public void setSender_address_id(int sender_address_id) {
        this.sender_address_id = sender_address_id;
    }

    public int getGetter_address_id() {
        return getter_address_id;
    }

    public void setGetter_address_id(int getter_address_id) {
        this.getter_address_id = getter_address_id;
    }

    public List<Commodity> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<Commodity> commodityList) {
        this.commodityList = commodityList;
    }
}
