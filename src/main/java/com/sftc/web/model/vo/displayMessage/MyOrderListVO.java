package com.sftc.web.model.vo.displayMessage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * 我的订单列表界面展示类
 */
@ApiModel(value = "我的订单列表响应对象")
public class MyOrderListVO {

    @Setter @Getter
    @ApiModelProperty(name = "id",value = "订单id",example = "C1510544211749T8")
    private String id;

    @Setter @Getter
    @ApiModelProperty(name = "sender_name",value = "寄件人姓名",example = "测试单")
    private String sender_name;

    @Setter @Getter
    @ApiModelProperty(name = "sender_addr",value = "寄件人地址",example = "深圳市软件产业基地1栋3楼")
    private String sender_addr;

    @Setter @Getter
    @ApiModelProperty(name = "order_type",value = "订单类型",example = "ORDER_BASIS")
    private String order_type;

    @Setter @Getter
    @ApiModelProperty(name = "region_type",value = "订单区域类型",example = "REGION_SAME")
    private String region_type;

    @ApiModelProperty(name = "is_gift",value = "是否有贺卡",example = "true",dataType = "boolean")
    private boolean is_gift;

    @ApiModelProperty(name = "is_evaluated",value = "是否评价",example = "false",dataType = "boolean")
    private boolean is_evaluated;

    @Setter @Getter
    @ApiModelProperty(name = "pay_method",value = "支付方式",example = "FREIGHT_COLLECT")
    private String pay_method;

    @Setter @Getter
    @ApiModelProperty(name = "order_number",value = "订单号",example = "DB1624464275")
    private String order_number; // 单包裹的时候，从express里提出来

    @Setter @Getter
    @ApiModelProperty(name = "expressList",value = "快递数组")
    private List<OrderCallbackExpress> expressList;

    public class OrderCallbackExpress {
        @Setter @Getter
        @ApiModelProperty(name = "ship_name",value = "收件人姓名",example = "测试订单")
        private String ship_name;

        @Setter @Getter
        @ApiModelProperty(name = "ship_addr",value = "收件人地址",example = "龙城广场地铁站")
        private String ship_addr;

        @Setter   @Getter
        @ApiModelProperty(name = "uuid",value = "订单uuid",example = "2c9a85895fb5320c015fb56627b80169")
        private String uuid;

        @Setter   @Getter
        @ApiModelProperty(name = "state",value = "订单状态",example = "WAIT_HAND_OVER")
        private String state;

        @Setter   @Getter
        @ApiModelProperty(name = "attributes",value = "订单attributes信息")
        private String attributes;

        @Setter   @Getter
        @ApiModelProperty(name = "order_number",value = "订单号",example = "DB1624464275")
        private String order_number;

        @Setter   @Getter
        @ApiModelProperty(name = "package_type",value = "包裹大小类型",example = "0")
        private String package_type; //为C端小程序的物品类型

        @Setter   @Getter
        @ApiModelProperty(name = "object_type",value = "包裹类型",example = "FILE")
        private String object_type;  // 包裹的类型

        @Setter   @Getter
        @ApiModelProperty(name = "package_comments",value = "包裹描述",example = "大家电测试包裹描述")
        private String package_comments; //当物品类型在packageType罗列的类型之外时填写

        @Setter   @Getter
        @ApiModelProperty(name = "reserve_time",value = "预约时间",example = "1509538551263")
        private String reserve_time;

        @Setter   @Getter
        @ApiModelProperty(name = "directed_code",value = "取件码",example = "578211")
        private String directed_code;  // 取件码

        @Setter   @Getter
        @ApiModelProperty(name = "weight",value = "包裹重量",example = "3")
        private String weight;
    }

    public boolean isIs_gift() {return is_gift;}

    public void setIs_gift(boolean is_gift) {this.is_gift = is_gift;}

    public boolean isIs_evaluated() {return is_evaluated;}

    public void setIs_evaluated(boolean is_evaluated) {
        this.is_evaluated = is_evaluated;
    }
}
