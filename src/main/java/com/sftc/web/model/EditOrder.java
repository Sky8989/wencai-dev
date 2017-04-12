package com.sftc.web.model;

/**
 * Created by Administrator on 2017/4/12.
 */
public class EditOrder {
    private int id;
    private String userName;
    private String postalCode;
    private String provinceName;
    private String cityName;
    private String countyName;
    private String detailInfo;
    private String nationlCode;
    private String telNumber;

    public EditOrder(String userName, String postalCode, String provinceName,
                     String cityName, String countyName, String detailInfo, String nationlCode, String telNumber) {
        this.userName = userName;
        this.postalCode = postalCode;
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.countyName = countyName;
        this.detailInfo = detailInfo;
        this.nationlCode = nationlCode;
        this.telNumber = telNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getNationlCode() {
        return nationlCode;
    }

    public void setNationlCode(String nationlCode) {
        this.nationlCode = nationlCode;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }
}
