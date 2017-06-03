package com.sftc.web.model.apiCallback;

import com.sftc.web.model.GiftCard;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model.apiCallback
 * @Description: 联系人来往接口model
 * @date 2017/6/3
 * @Time 下午5:01
 */
public class ContactCallback {

    private int id;
    // 寄件人姓名
    private String sender_name;
    // 寄件人的用户id（用于查询该用户的头像）
    private int sender_user_id;
    // 寄件人的头像
    private String sender_icon;
    // 收件人的姓名
    private String ship_name;
    // 收件人的用户id（用于查询该用户的头像）
    private int ship_user_id;
    // 收件人的头像
    private String ship_icon;
    // 创建时间
    private String create_time;
    private GiftCard giftCard;
    // 订单的状态
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public int getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(int sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public String getSender_icon() {
        return sender_icon;
    }

    public void setSender_icon(String sender_icon) {
        this.sender_icon = sender_icon;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public int getShip_user_id() {
        return ship_user_id;
    }

    public void setShip_user_id(int ship_user_id) {
        this.ship_user_id = ship_user_id;
    }

    public String getShip_icon() {
        return ship_icon;
    }

    public void setShip_icon(String ship_icon) {
        this.ship_icon = ship_icon;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public GiftCard getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(GiftCard giftCard) {
        this.giftCard = giftCard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
