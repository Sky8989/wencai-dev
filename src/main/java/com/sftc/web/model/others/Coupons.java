package com.sftc.web.model.others;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/18.
 */
@ApiModel(value = "优惠券信息")
public class Coupons {

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "优惠券uuid",example = "2c9a85895f24480d015f28a798b33db2")
    private String uuid;

    @Getter @Setter
    @ApiModelProperty(name = "type",value = "优惠券type",example = "DIRECT_OFF")
    private String type;

    @Getter @Setter
    @ApiModelProperty(name = "money",value = "优惠券money",example = "3000",dataType = "int")
    private int money;

    @Getter @Setter
    @ApiModelProperty(name = "status",value = "优惠券状态",example = "USED")
    private String status;

    @Getter @Setter
    @ApiModelProperty(name = "description",value = "优惠券描述",example = "券面信息")
    private String description;

    @Getter @Setter
    @ApiModelProperty(name = "attributes",value = "优惠限制标签")
    private CouponAttributes attributes;

    @Getter @Setter
    @ApiModelProperty(name = "user_uuid",value = "用户uuid",example = "2c9a85895d82ebe7015d8d4c6cc11df6")
    private String user_uuid;

    @Getter @Setter
    @ApiModelProperty(name = "start_time",value = "开始时间",example = "2017-10-17T12:44:00.000+0800")
    private String start_time;

    @Getter @Setter
    @ApiModelProperty(name = "expire_time",value = "有效时间",example = "2018-10-17T12:44:00.000+0800")
    private String expire_time;

    @Getter @Setter
    @ApiModelProperty(name = "created_at",value = "兑换时间",example = "2017-10-17T12:46:33.000+0800")
    private String created_at;

    @Getter @Setter
    @ApiModelProperty(name = "used_at",value = "使用时间",example = "2017-11-16T14:20:31.000+0800")
    private String used_at;

    @Getter @Setter
    @ApiModelProperty(name = "promo_code_uuid",value = "promo_code_uuid",example = "2c9a85895f24480d015f28a798083c21")
    private String promo_code_uuid;

    @Getter @Setter
    @ApiModelProperty(name = "real_price",value = "优惠券金额",example = "0",dataType = "int")
    private int real_price;

    @Getter @Setter
    @ApiModelProperty(name = "discount",value = "优惠券折扣",example = "0",dataType = "int")
    private int discount;

    @Getter @Setter
    @ApiModelProperty(name = "metadata",value = "metadata")
    private String metadata;
}
