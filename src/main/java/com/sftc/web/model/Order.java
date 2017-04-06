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
    private String orderNumber;
    // 订单状态
    private String state;
    // 下单时间
    private Date orderTime;
    // 支付时间
    private Date payTime;
    // 付款方式
    private String paymentMethod;
    //运费
    private double commodityPrice;
    // 寄件人id(根据用户表id)
    private User user;
    private int userId;
    // 地址表id
    private Address address;
    private int senderAddressId;
    // 地址表id
    private int getterAddressId;
    private List<Commodity> commodityList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(double commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getSenderAddressId() {
        return senderAddressId;
    }

    public void setSenderAddressId(int senderAddressId) {
        this.senderAddressId = senderAddressId;
    }

    public int getGetterAddressId() {
        return getterAddressId;
    }

    public void setGetterAddressId(int getterAddressId) {
        this.getterAddressId = getterAddressId;
    }

    public List<Commodity> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<Commodity> commodityList) {
        this.commodityList = commodityList;
    }
}
