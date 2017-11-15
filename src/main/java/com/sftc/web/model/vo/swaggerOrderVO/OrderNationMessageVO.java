package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "大网订单信息")
public class OrderNationMessageVO {
    @ApiModelProperty(name = "sender_user_id",value = "寄件人id",example = "10028",required = true)
    private String sender_user_id;
    @ApiModelProperty(name = "gift_card_id",value = "礼品卡id",example = "1")
    private String gift_card_id;
    @ApiModelProperty(name = "voice_time",value = "语音时长",example = "12")
    private String voice_time;
    @ApiModelProperty(name = "word_message",value = "文本寄语",example = "一句留言")
    private String word_message;
    @ApiModelProperty(name = "image",value = "包裹图片",example = "一张图片")
    private String image;
    @ApiModelProperty(name = "voice",value = "语音",example = "一条语音")
    private String voice;
    @ApiModelProperty(name = "reserve_time",value = "预约时间",example = "1501497840000")
    private String reserve_time;
    @ApiModelProperty(name = "form_id",example = "1500867486227")
    private String form_id;
    @ApiModelProperty(name = "j_longitude",example = "114.25885")
    private String j_longitude;
    @ApiModelProperty(name = "j_latitude",example = "22.72007")
    private String j_latitude;
    @ApiModelProperty(name = "d_longitude",example = "114.25885")
    private String d_longitude;
    @ApiModelProperty(name = "d_latitude",example = "22.72007")
    private String d_latitude;

    public String getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(String sender_user_id) {this.sender_user_id = sender_user_id;}

    public String getGift_card_id() {return gift_card_id;}

    public void setGift_card_id(String gift_card_id) {
        this.gift_card_id = gift_card_id;
    }

    public String getVoice_time() {
        return voice_time;
    }

    public void setVoice_time(String voice_time) {
        this.voice_time = voice_time;
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

    public String getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(String reserve_time) {
        this.reserve_time = reserve_time;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public String getJ_longitude() {
        return j_longitude;
    }

    public void setJ_longitude(String j_longitude) {
        this.j_longitude = j_longitude;
    }

    public String getJ_latitude() {
        return j_latitude;
    }

    public void setJ_latitude(String j_latitude) {
        this.j_latitude = j_latitude;
    }

    public String getD_longitude() {
        return d_longitude;
    }

    public void setD_longitude(String d_longitude) {
        this.d_longitude = d_longitude;
    }

    public String getD_latitude() {
        return d_latitude;
    }

    public void setD_latitude(String d_latitude) {
        this.d_latitude = d_latitude;
    }
}
