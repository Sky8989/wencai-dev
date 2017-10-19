package com.sftc.web.model.entity;

import com.sftc.tools.api.APIRequest;
import com.sftc.web.model.Evaluate;
import com.sftc.web.model.Object;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

@Entity
@Table(name = "sftc_order_express")
public class OrderExpress extends Object {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    // 门牌号
    private String supplementary_info;
    //取件码
    private String directed_code;
    // 是否面对面下单
    private int is_directed;

    private String attributes;

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getDirected_code() {
        return directed_code;
    }

    public void setDirected_code(String directed_code) {
        this.directed_code = directed_code;
    }

    public int getIs_directed() {
        return is_directed;
    }

    public void setIs_directed(int is_directed) {
        this.is_directed = is_directed;
    }

    // 包裹类型
    private String package_type;
    // 物品类型
    private String object_type;
    // 包裹补充描述信息
    private String package_comments;
    // 订单状态
    private String state;
    // 是否已经填写
    private int is_use;

    private int sender_user_id;
    //预约时间
    private String reserve_time;
    //包裹获取时间，指好友填写地址时的时间
    private String receive_time;
    // 下单到顺丰的时间 用于记录下单成功时的时间
    private String order_time;
    // 收件人id(根据用户表id)
    private int ship_user_id;
    private Double latitude;
    private Double longitude;
    private String order_id;

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

    public OrderExpress(String order_time, String create_time, String order_number, String ship_name, String ship_mobile, String ship_province,
                        String ship_city, String ship_area, String ship_addr, String supplementary_info, String package_type, String object_type,
                        String package_comments, String state, int sender_user_id, String order_id, String uuid, Double latitude, Double longitude, String directed_code,
                        String attributes, int is_directed) {
        this.order_time = order_time;
        this.create_time = create_time;
        this.order_number = order_number;
        this.ship_name = ship_name;
        this.ship_mobile = ship_mobile;
        this.ship_province = ship_province;
        this.ship_city = ship_city;
        this.ship_area = ship_area;
        this.ship_addr = ship_addr;
        this.supplementary_info = supplementary_info;
        this.package_type = package_type;
        this.object_type = object_type;
        this.package_comments = package_comments;//增加快递描述
        this.state = state;
        this.sender_user_id = sender_user_id;
        this.order_id = order_id;
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.directed_code = directed_code;
        this.attributes = attributes;
        this.is_directed = is_directed;
    }

    public OrderExpress(String order_time, String create_time, String order_number, String ship_name, String ship_mobile, String ship_province,
                        String ship_city, String ship_area, String ship_addr, String supplementary_info, String package_type, String object_type,
                        String package_comments, String state, int sender_user_id, String order_id, String uuid, Double latitude, Double longitude) {
        this.order_time = order_time;
        this.create_time = create_time;
        this.order_number = order_number;
        this.ship_name = ship_name;
        this.ship_mobile = ship_mobile;
        this.ship_province = ship_province;
        this.ship_city = ship_city;
        this.ship_area = ship_area;
        this.ship_addr = ship_addr;
        this.supplementary_info = supplementary_info;
        this.package_type = package_type;
        this.object_type = object_type;
        this.package_comments = package_comments;//增加快递描述
        this.state = state;
        this.sender_user_id = sender_user_id;
        this.order_id = order_id;
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public OrderExpress(String order_time, String create_time, String order_number, String ship_name, String ship_mobile, String ship_province,
                        String ship_city, String ship_area, String ship_addr, String package_type, String object_type,
                        String state, int sender_user_id, String order_id, String uuid, Double latitude, Double longitude) {
        this.order_time = order_time;
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

    public OrderExpress(String uuid, String order_id, Double longitude, Double latitude, String state) {
        this.state = state;
        this.order_id = order_id;
        this.uuid = uuid;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public OrderExpress(String state, String uuid) {
        this.state = state;
        this.uuid = uuid;
    }

    public OrderExpress(String state, String uuid, String attributes) {
        this.state = state;
        this.uuid = uuid;
        this.attributes = attributes;
    }

    public OrderExpress(String order_number, String package_type, String object_type,
                        String order_id, String create_time, int is_use, String state, String uuid, int sender_user_id, String reserve_time) {
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
        this.reserve_time = reserve_time;
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

    public OrderExpress(APIRequest request, String order_number) {
        this.create_time = Long.toString(System.currentTimeMillis());
        this.order_number = order_number;
        this.ship_name = (String) request.getParameter("ship_name");
        this.ship_mobile = (String) request.getParameter("ship_mobile");
        this.ship_province = (String) request.getParameter("ship_province");
        this.ship_city = (String) request.getParameter("ship_city");
        this.ship_area = (String) request.getParameter("ship_area");
        this.ship_addr = (String) request.getParameter("ship_addr");
        this.package_type = (String) request.getParameter("package_type");
        this.object_type = (String) request.getParameter("object_type");
    }

    // cms 通过
    public OrderExpress(HttpServletRequest request) {
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
            this.id = Integer.parseInt(request.getParameter("id"));
        }
        if (request.getParameter("uuid") != null && !"".equals(request.getParameter("uuid"))) {
            this.uuid = request.getParameter("uuid");
        }
        if (request.getParameter("ship_mobile") != null && !"".equals(request.getParameter("ship_mobile"))) {
            this.ship_mobile = request.getParameter("ship_mobile");
        }
        if (request.getParameter("order_id") != null && !"".equals(request.getParameter("order_id"))) {
            this.order_id = request.getParameter("order_id");
        }
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


    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
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

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getSupplementary_info() {
        return supplementary_info;
    }

    public void setSupplementary_info(String supplementary_info) {
        this.supplementary_info = supplementary_info;
    }

    public String getPackage_comments() {
        return package_comments;
    }

    public void setPackage_comments(String package_comments) {
        this.package_comments = package_comments;
    }
}