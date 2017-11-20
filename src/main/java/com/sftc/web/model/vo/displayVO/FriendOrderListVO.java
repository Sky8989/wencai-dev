package com.sftc.web.model.vo.displayVO;

import java.util.List;

import com.sftc.web.enumeration.express.ObjectType;
import com.sftc.web.enumeration.express.OrderExpressState;
import com.sftc.web.enumeration.express.PackageType;
import com.sftc.web.enumeration.order.PayMethod;
import com.sftc.web.enumeration.order.RegionType;

import lombok.Getter;
import lombok.Setter;
/**
 * 好友圈订单列表界面展示类
 */

public class FriendOrderListVO {

    @Setter   @Getter
    private String id;

    @Setter @Getter
    private String sender_avatar;

    @Setter   @Getter
    private String sender_name;

    @Setter   @Getter
    private int sender_user_id;

    @Setter   @Getter
    private RegionType region_type;

    @Setter   @Getter
    private ObjectType object_type; // 物品类型
//    private String object_type; // 物品类型

    @Setter   @Getter
    private String word_message;// 文字寄语
    @Setter   @Getter
    private String image;       // 包裹图片
    @Setter   @Getter
    private String create_time;

    private boolean is_gift;

    private boolean is_evaluated;

    @Setter   @Getter
    private PayMethod pay_method;//支付类型
//    private String pay_method;//支付类型
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
        private String ship_avatar;
        @Setter   @Getter
        private int ship_user_id;
        @Setter   @Getter
        private String uuid;
        @Setter   @Getter
        private OrderExpressState state;
        @Setter   @Getter
        private String attributes;
        @Setter   @Getter
        private PackageType package_type; //为C端小程序的物品类型
        @Setter   @Getter
        private ObjectType object_type; // 包裹的类型
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
