package com.sftc.web.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.web.model
 * @Description: 快递订单表
 * @date 2017/5/9
 * @Time 下午4:55
 */
public class OrderExpress {

    private int id;
    // 创建时间
    private String create_time;
    // 订单编号
    private String order_number;
    // 收件人姓名
    private String ship_name;
    // 收件人手机
    private String ship_mobile;
    // 收件人省
    private String ship_province;
    // 收件人市
    private String ship_city;
    // 收件人区
    private String ship_area;
    // 收件人详细地址
    private String ship_addr;
    // 包裹类型
    private String package_type;
    // 物品类型
    private String object_type;

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

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public String getShip_mobile() {
        return ship_mobile;
    }

    public void setShip_mobile(String ship_mobile) {
        this.ship_mobile = ship_mobile;
    }

    public String getShip_province() {
        return ship_province;
    }

    public void setShip_province(String ship_province) {
        this.ship_province = ship_province;
    }

    public String getShip_city() {
        return ship_city;
    }

    public void setShip_city(String ship_city) {
        this.ship_city = ship_city;
    }

    public String getShip_area() {
        return ship_area;
    }

    public void setShip_area(String ship_area) {
        this.ship_area = ship_area;
    }

    public String getShip_addr() {
        return ship_addr;
    }

    public void setShip_addr(String ship_addr) {
        this.ship_addr = ship_addr;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }
}
