package com.sftc.web.model.reqeustParam;

import com.sftc.web.model.OrderExpress;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model.reqeustParam
 * @Description: 订单的包装类
 * @date 2017/5/15
 * @Time 上午10:48
 */
public class OrderParam {

    // 订单状态
    private String state;
    // 寄件人姓名
    private String sender_name;
    // 寄件人手机
    private String sender_mobile;
    // 寄件人省
    private String sender_province;
    // 寄件人市
    private String sender_city;
    // 寄件人详细地址
    private String sender_area;
    // 寄件人详细地址
    private String sender_addr;
    // 包裹图片
    private String image;
    // 语音
    private String voice;
    // 付款方式
    private String pay_method;
    // 配送方式
    private String distribution_method;
    // 经度
    private double longitude;
    // 纬度
    private double latitude;
    // 文字寄语
    private String word_message;
    // 剩余包裹数量
    private int package_count;
    // 快递员编号
    private String job_number;
    // 寄件人id(根据用户表id)
    private int sender_user_id;
    // 收件人id(根据用户表id)
    private int ship_user_id;
    // 礼卡表id
    private int gift_card_id;
    //语音时长
    private int voice_time;
    private OrderExpress orderExpress;

    public OrderExpress getOrderExpress() {
        return orderExpress;
    }

    public void setOrderExpress(OrderExpress orderExpress) {
        this.orderExpress = orderExpress;
    }

    private List<OrderExpress> orderExpressList;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_mobile() {
        return sender_mobile;
    }

    public void setSender_mobile(String sender_mobile) {
        this.sender_mobile = sender_mobile;
    }

    public String getSender_province() {
        return sender_province;
    }

    public void setSender_province(String sender_province) {
        this.sender_province = sender_province;
    }

    public String getSender_city() {
        return sender_city;
    }

    public void setSender_city(String sender_city) {
        this.sender_city = sender_city;
    }

    public String getSender_area() {
        return sender_area;
    }

    public void setSender_area(String sender_area) {
        this.sender_area = sender_area;
    }

    public String getSender_addr() {
        return sender_addr;
    }

    public void setSender_addr(String sender_addr) {
        this.sender_addr = sender_addr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getDistribution_method() {
        return distribution_method;
    }

    public void setDistribution_method(String distribution_method) {
        this.distribution_method = distribution_method;
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

    public String getWord_message() {
        return word_message;
    }

    public void setWord_message(String word_message) {
        this.word_message = word_message;
    }

    public int getPackage_count() {
        return package_count;
    }

    public void setPackage_count(int package_count) {
        this.package_count = package_count;
    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public int getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(int sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public int getShip_user_id() {
        return ship_user_id;
    }

    public void setShip_user_id(int ship_user_id) {
        this.ship_user_id = ship_user_id;
    }

    public int getGift_card_id() {
        return gift_card_id;
    }

    public void setGift_card_id(int gift_card_id) {
        this.gift_card_id = gift_card_id;
    }

    public List<OrderExpress> getOrderExpressList() {
        return orderExpressList;
    }

    public void setOrderExpressList(List<OrderExpress> orderExpressList) {
        this.orderExpressList = orderExpressList;
    }

    public int getVoice_time() {
        return voice_time;
    }

    public void setVoice_time(int voice_time) {
        this.voice_time = voice_time;
    }
}
