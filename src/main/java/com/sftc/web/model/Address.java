package com.sftc.web.model;

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
    private String district;
    // 楼层号或门派号
    private String detail;
    // 电话
    private String phone;
    // 收件人姓名
    private String name;
    // 所属的用户
    private User user;
    private int user_id;
    private List<com.sftc.web.model.Order> orderList;

    public Address() {
    }

    public Address(String province, String city, String district, String detail, String phone,int user_id,String name) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.phone = phone;
        this.name = name;
        this.user_id = user_id;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public List<com.sftc.web.model.Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<com.sftc.web.model.Order> orderList) {
        this.orderList = orderList;
    }
}
