package com.sftc.web.model.dto;

import com.sftc.web.model.Evaluate;
import com.sftc.web.model.PackageMessage;
import com.sftc.web.model.entity.OrderExpress;

import java.util.List;

public class OrderExpressDTO extends OrderExpress {

    // 礼卡表id
    private int gift_card_id;

    // extension 收件人头像
    private String ship_avatar;

    // 评价信息
    private Evaluate evaluate;

    private List<OrderExpress> orderExpressList;
    //包裹信息  名称/重量/类型
    private PackageMessage packageMessage;

    public List<OrderExpress> getOrderExpressList() {
        return orderExpressList;
    }

    public void setOrderExpressList(List<OrderExpress> orderExpresses) {
        this.orderExpressList = orderExpresses;
    }

    public OrderExpressDTO() {
    }

    public int getGift_card_id() {
        return gift_card_id;
    }

    public void setGift_card_id(int gift_card_id) {this.gift_card_id = gift_card_id;}

    public PackageMessage getPackageMessage() {return packageMessage;}

    public void setPackageMessage(PackageMessage packageMessage) {this.packageMessage = packageMessage;}

    public String getShip_avatar() {
        return ship_avatar;
    }

    public void setShip_avatar(String ship_avatar) {
        this.ship_avatar = ship_avatar;
    }

    public Evaluate getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Evaluate evaluate) {
        this.evaluate = evaluate;
    }

}