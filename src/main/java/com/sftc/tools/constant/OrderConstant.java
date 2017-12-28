package com.sftc.tools.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class OrderConstant {
    /**
     * 普通同城订单详情页面link
     * 普通单包裹:  					"pages/module_order/order_detail/index?order_id=order_id&uuid=uuid"
     */
    public static String BASIS_REGION_SAME_LINK ;
    /**
     * 好友同城订单详情页面link
     * 好友单包裹  						"pages/module_order/order_detail/index?order_id=order_id&uuid=uuid"
     * 好友&&多包裹&&一个人领取下单  	"pages/module_order/order_detail/index?order_id=order_id&uuid=uuid"
     */
    public static String MYSTERY_REGION_SAME_LINK;

    /**
     * 好友&&多包裹&&多人领取下单:  	"pages/module_order/friend_order_detail/index?order_id=order_id"
     */
    public static String MAMY_MYSTERY_REGION_SAME_LINK;

    @Value("${basis.region.same.link}")
    public static void setBasisRegionSameLink(String basisRegionSameLink) {
        BASIS_REGION_SAME_LINK = basisRegionSameLink;
    }

    @Value("${mystery.region.same.link}")
    public static void setMysteryRegionSameLink(String mysteryRegionSameLink) {
        MYSTERY_REGION_SAME_LINK = mysteryRegionSameLink;
    }

    @Value("${many.mystery.region.same.link}")
    public static void setMamyMysteryRegionSameLink(String mamyMysteryRegionSameLink) {
        MAMY_MYSTERY_REGION_SAME_LINK = mamyMysteryRegionSameLink;
    }
}