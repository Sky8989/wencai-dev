package com.sftc.web.model.vo.displayMessage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * 我的订单列表界面展示类
 */
public class MyOrderListVO {
    @Setter @Getter
    private String id;

    @Setter @Getter
    private String sender_name;

    @Setter @Getter
    private String sender_addr;

    @Setter @Getter
    private String order_type;

    @Setter @Getter
    private String region_type;

    private boolean is_gift;

    private boolean is_evaluated;

    @Setter @Getter
    private String pay_method;

    @Setter @Getter
    private String order_number; // 单包裹的时候，从express里提出来

    @Setter @Getter
    private List<OrderCallbackExpress> expressList;

    public class OrderCallbackExpress {
        @Setter @Getter
        private String ship_name;

        @Setter @Getter
        private String ship_addr;

        @Setter   @Getter
        private String uuid;

        @Setter   @Getter
        private String route_state;

        @Setter   @Getter
        private String pay_state;

        @Setter   @Getter
        private String attributes;

        @Setter   @Getter
        private String order_number;

        @Setter   @Getter
        private String package_type; //为C端小程序的物品类型

        @Setter   @Getter
        private String object_type;  // 包裹的类型

        @Setter   @Getter
        private String package_comments; //当物品类型在packageType罗列的类型之外时填写

        @Setter   @Getter
        private String reserve_time;

        @Setter   @Getter
        private String directed_code;  // 取件码

        @Setter   @Getter
        private String weight;
    }

    public boolean isIs_gift() {return is_gift;}

    public void setIs_gift(boolean is_gift) {this.is_gift = is_gift;}

    public boolean isIs_evaluated() {return is_evaluated;}

    public void setIs_evaluated(boolean is_evaluated) {
        this.is_evaluated = is_evaluated;
    }
}
