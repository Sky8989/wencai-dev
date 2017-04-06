package com.sftc.ssm.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 地址类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class Address {

    private int id ;
    // 省份
    private String province;
    // 城市
    private String city;
    // 小区
    private String block;
    // 楼层号或门派号
    private String houseNumber;
    // 电话
    private String phone;
    // 收件人姓名
    private String name;
    // 地址标记（寄还是收）
    private String addressTap;
    // 所属的用户
    private User user;
    private int userId;
    private List<Order> orderList;

    public Address() {
    }

    public Address(String province, String city, String block, String houseNumber,
                   String phone, String name, String addressTap, int userId) {


        this.province = province;
        this.city = city;
        this.block = block;
        this.houseNumber = houseNumber;
        this.phone = phone;
        this.name = name;
        this.addressTap = addressTap;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressTap() {
        return addressTap;
    }

    public void setAddressTap(String addressTap) {
        this.addressTap = addressTap;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
