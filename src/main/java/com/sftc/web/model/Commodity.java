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
    private String commodity_name;
    // 商品重量
    private double commodity_weight;
    // 商品图片
    private String commodity_photo;
    // 商品描述
    private String commodity_instruction;
    // 所属的订单
    private Order order;
    //所属订单ID
    private int order_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public double getCommodity_weight() {
        return commodity_weight;
    }

    public void setCommodity_weight(double commodity_weight) {
        this.commodity_weight = commodity_weight;
    }

    public String getCommodity_photo() {
        return commodity_photo;
    }

    public void setCommodity_photo(String commodity_photo) {
        this.commodity_photo = commodity_photo;
    }

    public String getCommodity_instruction() {
        return commodity_instruction;
    }

    public void setCommodity_instruction(String commodity_instruction) {
        this.commodity_instruction = commodity_instruction;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
