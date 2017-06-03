package com.sftc.web.model;

import com.sftc.tools.api.APIRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.web.model
 * @Description: 快递订单表
 * @date 2017/5/9
 * @Time 下午4:55
 */
public class OrderExpress extends Object {

    private int id;
    // 创建时间
    private String create_time;
    // 订单编号
    private Long order_number;
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
    // 订单状态
    private String state;
    // 是否已经填写
    private int is_use;

    private int sender_user_id;
    //预约时间
    private String reserve_time;

    // 收件人id(根据用户表id)
    private int ship_user_id;
    // 礼卡表id
    private int gift_card_id;
    private Double latitude;
    private Double longitude;
    private int order_id;
    public int getSender_user_id() {
        return sender_user_id;
    }
    private String uuid;
    public void setSender_user_id(int sender_user_id) {
        this.sender_user_id = sender_user_id;
    }


    public OrderExpress() {
    }

    public OrderExpress(int id, String state) {
        this.id = id;
        this.state = state;
    }

    public OrderExpress(String create_time, Long order_number, String ship_name, String ship_mobile, String ship_province,
                        String ship_city, String ship_area, String ship_addr, String package_type, String object_type,
                        String state, int sender_user_id, int order_id,String uuid,Double latitude,Double longitude) {
        this.create_time = create_time;
        this.order_number = order_number;
        this.ship_name = ship_name;
        this.ship_mobile = ship_mobile;
        this.ship_province = ship_province;
        this.ship_city = ship_city;
        this.ship_area = ship_area;
        this.ship_addr = ship_addr;
        this.package_type = package_type;
        this.object_type = object_type;
        this.state = state;
        this.sender_user_id = sender_user_id;
        this.order_id = order_id;
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public OrderExpress(String uuid,int order_id,Double longitude,Double latitude,String state) {
        this.state=state;
       this.order_id=order_id;
        this.uuid = uuid;
        this.longitude=longitude;
        this.latitude=latitude;

    }

    // public OrderExpress(String package_type, String object_type) {
    //     this.package_type = package_type;
    //     this.object_type = object_type;
    // }

    public OrderExpress(String state, String uuid) {
        this.state = state;
        this.uuid = uuid;
    }
//    public OrderExpress(String package_type, String object_type) {
//        this.package_type = package_type;
//        this.object_type = object_type;
//    }

    public OrderExpress(Long order_number,String package_type, String object_type,
                        int order_id, String create_time, int is_use,String state,String uuid,int sender_user_id,String reserve_time) {
        this.order_number = order_number;
        this.package_type = package_type;
        this.object_type = object_type;
        this.sender_user_id = sender_user_id;
        this.ship_user_id = ship_user_id;
        this.create_time = create_time;
        this.is_use = is_use;
        this.order_id = order_id;
        this.state = state;
        this.uuid = uuid;
       this.reserve_time=reserve_time;
        this.sender_user_id = sender_user_id;
    }

    public OrderExpress(APIRequest request) {
        this.id = Integer.parseInt((String) request.getParameter("id"));
        this.ship_name = (String) request.getParameter("ship_name");
        this.ship_mobile = (String) request.getParameter("ship_mobile");
        this.ship_province = (String) request.getParameter("ship_province");
        this.ship_city = (String) request.getParameter("ship_city");
        this.ship_area = (String) request.getParameter("ship_area");
        this.ship_addr = (String) request.getParameter("ship_addr");
    }

    public OrderExpress(APIRequest request, Long order_number) {
        this.create_time = Long.toString(System.currentTimeMillis());
        this.order_number = order_number;
        this.ship_name = (String) request.getParameter("ship_name");
        this.ship_mobile = (String) request.getParameter("ship_mobile");
        this.ship_province = (String) request.getParameter("ship_province");
        this.ship_city = (String) request.getParameter("ship_city");
        this.ship_area = (String) request.getParameter("ship_area");
        this.ship_addr = (String) request.getParameter("ship_addr");
        this.package_type = (String)request.getParameter("package_type");
        this.object_type = (String) request.getParameter("object_type");
    }

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

    public Long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(Long order_number) {
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



    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public int getIs_use() {
        return is_use;
    }

    public void setIs_use(int is_use) {
        this.is_use = is_use;
    }


    public int getShip_user_id() {
        return ship_user_id;
    }

    public void setShip_user_id(int ship_user_id) {
        this.ship_user_id = ship_user_id;
    }

    public int getGift_card_id() {
        return gift_card_id;
    }

    public void setGift_card_id(int gift_card_id) {
        this.gift_card_id = gift_card_id;

    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(String reserve_time) {
        this.reserve_time = reserve_time;
    }
}