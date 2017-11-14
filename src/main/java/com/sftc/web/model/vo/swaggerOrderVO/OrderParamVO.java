package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.jdbc.Null;

@ApiModel(value = "寄件人订单填写包装类")
public class OrderParamVO {

    @ApiModelProperty(name = "ship_name",value = "寄件人姓名",example = "Joel测试订单1")
    private String sender_name;
    @ApiModelProperty(name = "sender_mobile",value = "寄件人电话",example = "13691698530")
    private String sender_mobile;
    @ApiModelProperty(name = "sender_province",value = "寄件人省份",example = "广东省")
    private String sender_province;
    @ApiModelProperty(name = "sender_city",value = "寄件人城市",example = "深圳市")
    private String sender_city;
    @ApiModelProperty(name = "sender_area",value = "寄件人区域",example = "龙岗区")
    private String sender_area;
    @ApiModelProperty(name = "supplementary_info",value = "寄件人门牌号",example = "寄件人填写1号")
    private String supplementary_info;
    @ApiModelProperty(name = "sender_addr",value = "寄件人详细地址",example = "大运")
    private String sender_addr;
    @ApiModelProperty(name = "image",value = "图片",example = "yqy.jpg")
    private String image;
    @ApiModelProperty(name = "voice",value = "语音",example = "你好")
    private String voice;
    @ApiModelProperty(name = "pay_method",value = "支付方式",example = "到付")
    private String pay_method;
    @ApiModelProperty(name = "distribution_method",value = "配送方式",example = "das")
    private String distribution_method;
    @ApiModelProperty(name = "longitude",value = "经度",example = "114.260976")
    private double longitude;
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.723223")
    private double latitude;
    @ApiModelProperty(name = "word_message",value = "留言",example = "你好")
    private String word_message;
    @ApiModelProperty(name = "package_count",value = "包裹数量",example = "1")
    private int package_count;
    @ApiModelProperty(name = "sender_user_id",value = "寄件人id",example = "10028")
    private int sender_user_id;
    @ApiModelProperty(name = "gift_card_id",value = "贺卡id",example = "1")
    private int gift_card_id;
    @ApiModelProperty(name = "voice_time",value = "语音时长",example = "10")
    private int voice_time;
    @ApiModelProperty(name = "package_comments",value = "快递描述",example = "补充快递信息")
    private String package_comments;
    @ApiModelProperty(name = "object_type",value = "物品类型",example = "dsa")
    private  String object_type;
    @ApiModelProperty(name = "package_type",value = "包裹类型",example = "dsa")
    private  String package_type;
    @ApiModelProperty(name = "order_type",value = "是否普通",example = "ORDER_MYSTERY")
    private String order_type;
    @ApiModelProperty(name = "region_type",value = "同城,大网",notes = "请将里面默认的String去掉，否则好友无法提交")
    private String region_type;

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

    public int getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(int sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public int getGift_card_id() {
        return gift_card_id;
    }

    public void setGift_card_id(int gift_card_id) {
        this.gift_card_id = gift_card_id;
    }

    public int getVoice_time() {
        return voice_time;
    }

    public void setVoice_time(int voice_time) {
        this.voice_time = voice_time;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getRegion_type() {
        return region_type;
    }

    public void setRegion_type(String region_type) {
        this.region_type = region_type;
    }

    public String getSupplementary_info() {return supplementary_info;}

    public void setSupplementary_info(String supplementary_info) {this.supplementary_info = supplementary_info;}

    public String getPackage_comments() {
        return package_comments;
    }

    public void setPackage_comments(String package_comments) {
        this.package_comments = package_comments;
    }
}
