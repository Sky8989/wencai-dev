package com.sftc.web.model;

import com.sftc.web.model.reqeustParam.OrderParam;

import java.util.List;

public class Order extends Object {

    private int id;
    // 创建时间
    private String create_time;
    // 订单编号
    private Long order_number;
    // 支付时间
    private String pay_time;
    // 付款方式
    private String pay_method;
    // 配送方式
    private String distribution_method;
    // 运费
    private double freight;
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
    // 文字寄语
    private String word_message;
    // 包裹图片
    private String image;
    // 语音
    private String voice;
    // 经度
    private double longitude;
    // 纬度
    private double latitude;
    // 寄件人id(根据用户表id)
    private User user;
    private int sender_user_id;
    // 礼卡表id
    private int gift_card_id;
    // 语音时间
    private int voice_time;
    private GiftCard giftCard;
    private List<OrderExpress> orderExpressList;
    private OrderExpress orderExpress;
    //订单类型 普通 神秘
    private String order_type;
    //订单地域 同城 大网
    private String region_type;

    public Order() {
    }

    public Order(double longitude, double latitude, int id) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Order(String create_time, Long order_number, String pay_method,
                 String distribution_method, double freight, String sender_name, String sender_mobile, String sender_province,
                 String sender_city, String sender_area, String sender_addr,
                 double longitude, double latitude, String order_type, int sender_user_id) {
        this.create_time = create_time;
        this.order_number = order_number;
        this.pay_method = pay_method;
        this.distribution_method = distribution_method;
        this.freight = freight;
        this.sender_name = sender_name;
        this.sender_mobile = sender_mobile;
        this.sender_province = sender_province;
        this.sender_city = sender_city;
        this.sender_area = sender_area;
        this.sender_addr = sender_addr;
        this.longitude = longitude;
        this.latitude = latitude;
        this.order_type = order_type;
        this.sender_user_id = sender_user_id;
    }

    public Order(OrderParam orderParam) {
        this.create_time = Long.toString(System.currentTimeMillis());
        this.order_number = (long) (Math.random() * 100000 * 1000000);
        this.pay_method = orderParam.getPay_method();
        this.distribution_method = orderParam.getDistribution_method();
        this.sender_name = orderParam.getSender_name();
        this.sender_mobile = orderParam.getSender_mobile();
        this.sender_province = orderParam.getSender_province();
        this.sender_city = orderParam.getSender_city();
        this.sender_area = orderParam.getSender_area();
        this.sender_addr = orderParam.getSender_addr();
        this.word_message = orderParam.getWord_message();
        this.image = orderParam.getImage();
        this.voice = orderParam.getVoice();
        this.longitude = orderParam.getLongitude();
        this.latitude = orderParam.getLatitude();
        this.voice_time = orderParam.getVoice_time();
        this.sender_user_id = orderParam.getSender_user_id();
        this.gift_card_id = orderParam.getGift_card_id();
        this.order_type = orderParam.getOrder_type();
        this.region_type = orderParam.getRegion_type();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(Long order_number) {
        this.order_number = order_number;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
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

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
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

    public String getWord_message() {
        return word_message;
    }

    public void setWord_message(String word_message) {
        this.word_message = word_message;
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

    public GiftCard getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(GiftCard giftCard) {
        this.giftCard = giftCard;
    }

    public int getGift_card_id() {
        return gift_card_id;
    }

    public void setGift_card_id(int gift_card_id) {
        this.gift_card_id = gift_card_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(int sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public List<OrderExpress> getOrderExpressList() {
        return orderExpressList;
    }

    public void setOrderExpressList(List<OrderExpress> orderExpressList) {
        this.orderExpressList = orderExpressList;
    }

    public OrderExpress getOrderExpress() {
        return orderExpress;
    }

    public void setOrderExpress(OrderExpress orderExpress) {
        this.orderExpress = orderExpress;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public int getVoice_time() {
        return voice_time;
    }

    public void setVoice_time(int voice_time) {
        this.voice_time = voice_time;
    }

    public String getRegion_type() {
        return region_type;
    }

    public void setRegion_type(String region_type) {
        this.region_type = region_type;
    }
}
