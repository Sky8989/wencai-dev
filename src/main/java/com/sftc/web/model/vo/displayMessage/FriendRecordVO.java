package com.sftc.web.model.vo.displayMessage;

import com.sftc.web.model.entity.OrderExpress;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * 来往记录界面展示类
 */
public class FriendRecordVO {
    @Setter @Getter
    private int id;

    @Setter @Getter
    private String sender_name;// 寄件人姓名

    @Setter @Getter
    private int sender_user_id; // 寄件人的用户id

    @Setter @Getter
    private String sender_icon;   // 寄件人的头像

    @Setter @Getter
    private String ship_name;  // 收件人的姓名

    @Setter @Getter
    private int ship_user_id;  // 收件人的用户id

    @Setter @Getter
    private String ship_icon;    // 收件人的头像

    @Setter @Getter
    private String object_type;   // 物品类型

    @Setter @Getter
    private String order_id;   // 订单编号

    @Setter @Getter
    private String state;   // 快递状态

    @Setter @Getter
    private int gift_card_id;   // 礼物卡id

    @Setter @Getter
    private String create_time;  // 创建时间

    @Setter @Getter
    private String region_type;

    @Setter @Getter
    private String sender_wechatname;    // 寄件人微信名

    @Setter @Getter
    private String ship_wechatname;    // 收件人微信名

    @Setter @Getter
    private String package_type;    //为C端小程序的物品类型

    @Setter @Getter
    private String pay_method;   //支付方式

    @Setter @Getter
    private String package_comments; //当物品类型在packageType罗列的类型之外时填写

    @Setter @Getter
    private String uuid;   //快递uuid

    @Setter @Getter
    private List<OrderExpress> orderExpressList;
}
