package com.sftc.web.model.dto;

import com.sftc.tools.api.APIRequest;
import com.sftc.web.model.Evaluate;
import com.sftc.web.model.Object;
import com.sftc.web.model.entity.OrderExpress;

import javax.servlet.http.HttpServletRequest;

public class OrderExpressDTO extends OrderExpress {

    // 礼卡表id
    private int gift_card_id;

    // extension 收件人头像
    private String ship_avatar;

    // 评价信息
    private Evaluate evaluate;

    public OrderExpressDTO() {
    }

    public int getGift_card_id() {
        return gift_card_id;
    }

    public void setGift_card_id(int gift_card_id) {this.gift_card_id = gift_card_id;}

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