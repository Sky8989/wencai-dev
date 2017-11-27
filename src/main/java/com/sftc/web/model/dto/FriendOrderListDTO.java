package com.sftc.web.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * 好友圈订单列表信息
 */

public class FriendOrderListDTO {

    @Setter   @Getter
    private String id;

    @Setter @Getter
    private String sender_avatar;

    @Setter   @Getter
    private String sender_name;

    @Setter   @Getter
    private int sender_user_id;

    @Setter   @Getter
    private String object_type; // 物品类型

    @Setter   @Getter
    private String word_message;// 文字寄语
    @Setter   @Getter
    private String image;       // 包裹图片
    @Setter   @Getter
    private String create_time;

    private boolean is_gift;

    private boolean is_evaluated;

    @Setter   @Getter
    private String pay_method;//支付类型
    @Setter   @Getter
    private List<OrderFriendCallbackExpress> expressList;

    public class OrderFriendCallbackExpress {
        @Setter   @Getter
        private int id;
        @Setter   @Getter
        private int user_contact_id;
        @Setter   @Getter
        private String ship_name;
        @Setter   @Getter
        private String order_number;
        @Setter   @Getter
        private String ship_avatar;
        @Setter   @Getter
        private int ship_user_id;
        @Setter   @Getter
        private String uuid;
        @Setter   @Getter
        private String route_state;
        @Setter   @Getter
        private String pay_state;
        @Setter   @Getter
        private String attributes;
        @Setter   @Getter
        private String package_type; //为C端小程序的物品类型
        @Setter   @Getter
        private String object_type; // 包裹的类型
        @Setter   @Getter
        private String package_comments;  //当物品类型在packageType罗列的类型之外时填写
        @Setter   @Getter
        private String reserve_time;
        @Setter   @Getter
        private String weight;
    }

    public boolean isIs_gift() {return is_gift;}

    public void setIs_gift(boolean is_gift) {this.is_gift = is_gift;}

    public boolean isIs_evaluated() {return is_evaluated;}

    public void setIs_evaluated(boolean is_evaluated) {this.is_evaluated = is_evaluated;}
}
