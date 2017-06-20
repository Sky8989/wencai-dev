package com.sftc.web.model.apiCallback;

public class OrderCallback {

    private int id;
    private String ship_name;
    private String ship_addr;
    private String sender_name;
    private String sender_addr;
    private String order_number;
    private String uuid;
    private String state;
    private String order_type;
    private transient int gift_card_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public String getShip_addr() {
        return ship_addr;
    }

    public void setShip_addr(String ship_addr) {
        this.ship_addr = ship_addr;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_addr() {
        return sender_addr;
    }

    public void setSender_addr(String sender_addr) {
        this.sender_addr = sender_addr;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public int getGift_card_id() {
        return gift_card_id;
    }

    public void setGift_card_id(int gift_card_id) {
        this.gift_card_id = gift_card_id;
    }
}
