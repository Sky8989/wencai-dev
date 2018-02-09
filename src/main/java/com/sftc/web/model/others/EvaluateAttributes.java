package com.sftc.web.model.others;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/18.
 */
@ApiModel(value = "评价返回信息对象")
public class EvaluateAttributes {

    @Getter @Setter
    @ApiModelProperty(name = "ongoing",example = "3073")
    private String ongoing;

    @Getter @Setter
    @ApiModelProperty(name = "description",value = "订单描述",example = "这是一条订单描述")
    private String description;

    @Getter @Setter
    @ApiModelProperty(name = "arrival_time",value = "到达时间",example = "2017-06-29T19:05:42.404+0800")
    private String arrival_time;

    @Getter @Setter
    @ApiModelProperty(name = "bespoken_time",value = "预约时间",example = "2017-05-25T18:39:21.830+0800")
    private String bespoken_time;

    @Getter @Setter
    @ApiModelProperty(name = "department_code",example = "755AS")
    private String department_code;

    @Getter @Setter
    @ApiModelProperty(name = "department_name",value = "区域名称",example = "南头")
    private String department_name;

    @Getter @Setter
    @ApiModelProperty(name = "delivered_verify_sms",value = "验证码",example = "0976")
    private String delivered_verify_sms;

    @Getter @Setter
    @ApiModelProperty(name = "predict_hand_over_at",value = "揽件时间",example = "2017-06-27T15:22:33.595+08:00")
    private String predict_hand_over_at;

    @Getter @Setter
    @ApiModelProperty(name = "merchant_comments",value = "评价内容",example = "测试评价内容")
    private String merchant_comments;

    @Getter @Setter
    @ApiModelProperty(name = "merchant_tags",value = "评价标签",example = "服务好评,速度超快")
    private String merchant_tags;

    @Getter @Setter
    @ApiModelProperty(name = "merchant_score",value = "评价星级",example = "5")
    private String merchant_score;

}
