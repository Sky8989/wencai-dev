package com.sftc.web.model;

/**
 * 寄件人地址簿
 * Created by huxingyue on 2017/7/26.
 */
public class AddressBook {
    private int id;
    // 用户编号
    private int user_id;
    // 寄件人姓名
    private String name;
    // 寄件人手机号
    private String phone;
    // 省份
    private String province;
    // 城市
    private String city;
    // 地区
    private String area;
    // 详细地址
    private String address;
    // 经度
    private double longitude;
    // 纬度
    private double latitude;
    // 创建时间
    private String create_time;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getUser_id() {return user_id;}

    public void setUser_id(int user_id) {this.user_id = user_id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getProvince() {return province;}

    public void setProvince(String province) {this.province = province;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public String getArea() {return area;}

    public void setArea(String area) {this.area = area;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public double getLongitude() {return longitude;}

    public void setLongitude(double longitude) {this.longitude = longitude;}

    public double getLatitude() {return latitude;}

    public void setLatitude(double latitude) {this.latitude = latitude;}

    public String getCreate_time() {return create_time;}

    public void setCreate_time(String create_time) {this.create_time = create_time;}

    public AddressBook() { super();}

    public AddressBook(int id, int user_id, String name, String phone, String province,
                       String city, String area, String address, double longitude,
                       double latitude, String create_time) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.create_time = create_time;
    }
}

