package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.web.model.vo.displayMessage.PackageMessageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "订单详情包裹信息")
public class OrderDetailExpressVO {

    @Getter @Setter
    @ApiModelProperty(name = "gift_card_id",value = "礼品卡id",example = "0",dataType = "int")
    public int gift_card_id;

    @Getter @Setter
    @ApiModelProperty(name = "gift_card_id",value = "包裹信息")
    public PackageMessageVO packageMessage;

    @Getter @Setter
    @ApiModelProperty(name = "id",value = "快递id",example = "9104",dataType = "int")
    public int id;

    @Getter @Setter
    @ApiModelProperty(name = "create_time",value = "创建时间",example = "1508130348092")
    public String create_time;

    @Getter @Setter
    @ApiModelProperty(name = "is_directed",value = "是否面对面",example = "0",dataType = "int")
    public int is_directed;

    @Getter @Setter
    @ApiModelProperty(name = "weight",value = "重量",example = "1")
    public String weight;

    @Getter @Setter
    @ApiModelProperty(name = "package_type",value = "包裹大小类型",example = "0")
    public String package_type;

    @Getter @Setter
    @ApiModelProperty(name = "object_type",value = "物品类型",example = "FILE")
    public String object_type;

    @Getter @Setter
    @ApiModelProperty(name = "package_comments",value = "快递描述")
    public String package_comments;

    @Getter @Setter
    @ApiModelProperty(name = "state",value = "包裹大小类型",example = "CANCELED")
    public String state;

    @Getter @Setter
    @ApiModelProperty(name = "is_use",value = "是否使用",example = "0",dataType = "int")
    public int is_use;

    @Getter @Setter
    @ApiModelProperty(name = "sender_user_id",value = "寄件人id",example = "10142",dataType = "int")
    public int sender_user_id;

    @Getter @Setter
    @ApiModelProperty(name = "reserve_time",value = "预约时间")
    public String reserve_time;

    @Getter @Setter
    @ApiModelProperty(name = "ship_user_id",value = "收件人id",example = "10085",dataType = "int")
    public int ship_user_id;

    @Getter @Setter
    @ApiModelProperty(name = "order_id",value = "订单编号",example = "C1508130348092UT")
    public String order_id;

    @Getter @Setter
    @ApiModelProperty(name = "uuid",value = "快递uuid",example = "2c9a85895d82ebe7015d86c0338e020d")
    public String uuid;
}
