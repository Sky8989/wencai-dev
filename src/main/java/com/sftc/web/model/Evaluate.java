package com.sftc.web.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 评价类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class Evaluate {

    private int id;
    // 创建时间
    private String create_time;
    // 评价留言
    private String message;
    // 标签
    private String label;
    // 评价时间
    private String gmt_create;
    // 评价星级
    private String star_level;
    // 评价人的用户id
    private User user;
    private int user_id;
    // 评价的是哪一个订单
    private Order order;
    private int order_id;
    // 快递员id
    private Courier courier;
    private int courier_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getStar_level() {
        return star_level;
    }

    public void setStar_level(String star_level) {
        this.star_level = star_level;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public int getCourier_id() {
        return courier_id;
    }

    public void setCourier_id(int courier_id) {
        this.courier_id = courier_id;
    }
}
