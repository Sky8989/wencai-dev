package com.sftc.web.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 商品类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class Commodity {

    private int id ;
    // 商品名称
    private String commodityName;
    // 商品重量
    private double commodityWeight;
    // 商品图片
    private String commodityPhoto;
    // 商品描述
    private String commodityInstruction;
    // 所属的订单
    private Order order;
    //所属订单ID
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public double getCommodityWeight() {
        return commodityWeight;
    }

    public void setCommodityWeight(double commodityWeight) {
        this.commodityWeight = commodityWeight;
    }

    public String getCommodityPhoto() {
        return commodityPhoto;
    }

    public void setCommodityPhoto(String commodityPhoto) {
        this.commodityPhoto = commodityPhoto;
    }

    public String getCommodityInstruction() {
        return commodityInstruction;
    }

    public void setCommodityInstruction(String commodityInstruction) {
        this.commodityInstruction = commodityInstruction;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
