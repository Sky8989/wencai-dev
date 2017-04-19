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
    private String nationalCode;
    private String telNumber;

    public EditOrder(String userName, String postalCode, String provinceName,
                     String cityName, String countyName, String detailInfo, String nationalCode, String telNumber) {
        this.userName = userName;
        this.postalCode = postalCode;
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.countyName = countyName;
        this.detailInfo = detailInfo;
        this.nationalCode = nationalCode;
        this.telNumber = telNumber;

    }





}
