package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Address;
import com.sftc.web.model.Object;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.reqeustParam.OrderParam;

public class AddressDTO extends Object {

    private Address addressEntity;

    private Long id;
    // 用户id
    private long user_id;
    // 用户名字
    private String name;
    // 用户电话
    private String phone;
    // 省份
    private String province;
    // 城市
    private String city;
    // 区
    private String area;
    // 具体地址
    private String address;
    // 门牌号等
    private String supplementary_info;
    // 经度
    private double longitude;
    // 纬度
    private double latitude;
    // 创建时间
    private String create_time;

    // 头像
    private String avatar;

    public Address getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(Address addressEntity) {
        this.addressEntity = addressEntity;
    }

    public AddressDTO() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSupplementary_info() {
        return supplementary_info;
    }

    public void setSupplementary_info(String supplementary_info) {
        this.supplementary_info = supplementary_info;
    }

    public AddressDTO(Long id, int user_id, String name, String phone, String province, String city, String area, String address, String create_time) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
        this.create_time = create_time;
    }

    public AddressDTO(OrderParam orderParam) {
        this.setUser_id(orderParam.getSender_user_id());
        this.setName(orderParam.getSender_name());
        this.setPhone(orderParam.getSender_mobile());
        this.setProvince(orderParam.getSender_province());
        this.setCity(orderParam.getSender_city());
        this.setArea(orderParam.getSender_area());
        this.setAddress(orderParam.getSender_addr());
        this.setArea(orderParam.getSender_area());
        this.setSupplementary_info(orderParam.getSupplementary_info());
        this.setLongitude(orderParam.getLongitude());
        this.setLatitude(orderParam.getLatitude());
        this.setCreate_time(Long.toString(System.currentTimeMillis()));
    }

    public AddressDTO(OrderExpress oe) {
        this.setUser_id(oe.getShip_user_id());
        this.setName(oe.getShip_name());
        this.setPhone(oe.getShip_mobile());
        this.setProvince(oe.getShip_province());
        this.setCity(oe.getShip_city());
        this.setArea(oe.getShip_area());
        this.setAddress(oe.getShip_addr());
        this.setArea(oe.getShip_area());
        this.setLongitude(oe.getLongitude());
        this.setLatitude(oe.getLatitude());
        this.setCreate_time(Long.toString(System.currentTimeMillis()));
    }

    public AddressDTO(int user_id, String name, String phone, String province, String city, String area, String address, String supplementary_info, double longitude, double latitude, String create_time) {
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
        this.supplementary_info = supplementary_info;
        this.longitude = longitude;
        this.latitude = latitude;
        this.create_time = create_time;
    }
}
