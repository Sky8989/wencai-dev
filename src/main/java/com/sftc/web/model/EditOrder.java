package com.sftc.web.model;

/**
 * Created by Administrator on 2017/4/12.
 */
public class EditOrder {
    private int id;
    private String user_name;
    private String postal_code;
    private String province_name;
    private String city_name;
    private String county_name;
    private String detail_info;
    private String national_code;
    private String tel_number;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCounty_name() {
        return county_name;
    }

    public void setCounty_name(String county_name) {
        this.county_name = county_name;
    }

    public String getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(String detail_info) {
        this.detail_info = detail_info;
    }

    public String getNational_code() {
        return national_code;
    }

    public void setNational_code(String national_code) {
        this.national_code = national_code;
    }

    public String getTel_number() {
        return tel_number;
    }

    public void setTel_number(String tel_number) {
        this.tel_number = tel_number;
    }
}
