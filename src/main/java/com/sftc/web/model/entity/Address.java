package com.sftc.web.model.entity;

import com.sftc.web.model.Object;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.reqeustParam.OrderParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sftc_address")
@ApiModel(value = "地址")
public class Address extends Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    private int id;

    @ApiModelProperty("用户id")
    private int user_id;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("补充地址")
    private String supplementary_info;

    @ApiModelProperty("经度")
    private double longitude;

    @ApiModelProperty("纬度")
    private double latitude;

    @ApiModelProperty("创建时间")
    private String create_time;

    public Address() {super();}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
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

    public String getSupplementary_info() {
        return supplementary_info;
    }

    public void setSupplementary_info(String supplementary_info) {
        this.supplementary_info = supplementary_info;
    }

    public Address(int id, int user_id, String name, String phone, String province, String city, String area, String address, String create_time) {
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

    public Address(OrderParam orderParam) {
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

    public Address(OrderExpress oe) {
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

    public Address(int user_id, String name, String phone, String province, String city, String area, String address, String supplementary_info, double longitude, double latitude, String create_time) {
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
