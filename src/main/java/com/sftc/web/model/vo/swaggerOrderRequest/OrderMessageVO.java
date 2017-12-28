package com.sftc.web.model.vo.swaggerOrderRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单信息")
public class OrderMessageVO {

    @Getter @Setter
    @ApiModelProperty(name = "gift_card_id",value = "礼品卡id",example = "1")
    @NotBlank(message = "gift_card_id参数不能为空")
    private String gift_card_id;

    @Getter @Setter
    @ApiModelProperty(name = "voice_time",value = "语音时长",example = "12")
    private String voice_time;

    @Getter @Setter
    @ApiModelProperty(name = "word_message",value = "文本寄语",example = "一句留言")
    private String word_message;

    @Getter @Setter
    @ApiModelProperty(name = "image",value = "包裹图片",example = "一张图片")
    private String image;

    @Getter @Setter
    @ApiModelProperty(name = "voice",value = "语音",example = "一条语音")
    private String voice;

    @Getter @Setter
    @ApiModelProperty(name = "reserve_time",value = "预约时间",example = "1501497840000")
    private String reserve_time;

    @Getter @Setter
    @ApiModelProperty(name = "form_id",required = true)
    private String form_id;
}
