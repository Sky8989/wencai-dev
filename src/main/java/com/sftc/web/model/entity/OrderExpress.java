package com.sftc.web.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import com.sftc.tools.EnumUtils;
import com.sftc.tools.api.APIRequest;
import com.sftc.web.enumeration.express.ObjectType;
import com.sftc.web.enumeration.express.OrderExpressState;
import com.sftc.web.enumeration.express.PackageType;
import com.sftc.web.model.others.Object;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sftc_order_express")
public class OrderExpress extends Object {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter @Getter
    private int id;

    @Setter @Getter
    private String create_time; // 创建时间

    @Setter @Getter
    private String order_number;// 订单编号

    @Setter @Getter
    private String ship_name;// 收件人姓名

    @Setter @Getter
    private String ship_mobile; // 收件人手机

    @Setter @Getter
    private String ship_province;// 收件人省

    @Setter @Getter
    private String ship_city; // 收件人市

    @Setter @Getter
    private String ship_area;// 收件人区

    @Setter @Getter
    private String ship_addr;// 收件人详细地址

    @Setter @Getter
    private String supplementary_info;// 门牌号

    @Setter @Getter
    private String directed_code;//取件码

    private int is_directed;// 是否面对面下单

    @Setter @Getter
    private String attributes;

    @Setter @Getter
    private String weight;

    @Setter @Getter
    private PackageType package_type;// 包裹类型
//    private String package_type;// 包裹类型

    @Setter @Getter
    private ObjectType object_type;// 物品类型
//    private String object_type;// 物品类型

    @Setter @Getter
    private String package_comments;// 包裹补充描述信息

    @Setter @Getter
    private OrderExpressState state;// 订单状态
//    private String state;// 订单状态

    @Setter @Getter
    private int is_use;// 是否已经填写

    @Setter @Getter
    private int sender_user_id;

    @Setter @Getter
    private String reserve_time; //预约时间

    @Setter @Getter
    private String receive_time;//包裹获取时间，指好友填写地址时的时间

    @Setter @Getter
    private String order_time;// 下单到顺丰的时间 用于记录下单成功时的时间

    @Setter @Getter
    private int ship_user_id;// 收件人id(根据用户表id)

    @Setter @Getter
    private Double latitude;

    @Setter @Getter
    private Double longitude;

    @Setter @Getter
    private String order_id;

    @Setter @Getter
    private String uuid;

    public OrderExpress() {
    }

    public OrderExpress(int id, OrderExpressState state) {
        this.id = id;
        this.state = state;
    }

    public OrderExpress(String order_time, String create_time, String order_number, String ship_name, String ship_mobile, String ship_province,
                        String ship_city, String ship_area, String ship_addr, String supplementary_info, String weight, ObjectType object_type,
                        String package_comments, OrderExpressState state, int sender_user_id, String order_id, String uuid, Double latitude, Double longitude,
                        String directed_code, String attributes, int is_directed,PackageType package_type) {
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
        this.weight = weight;
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
        this.package_type = package_type;
    }

    public OrderExpress(String order_time, String create_time, String order_number, String ship_name, String ship_mobile, String ship_province,
                        String ship_city, String ship_area, String ship_addr, String supplementary_info, String weight, ObjectType object_type,
                        String package_comments, OrderExpressState state, int sender_user_id, String order_id, String uuid, Double latitude, Double longitude,PackageType package_type) {
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
        this.weight = weight;
        this.object_type = object_type;
        this.package_comments = package_comments;//增加快递描述
        this.state = state;
        this.sender_user_id = sender_user_id;
        this.order_id = order_id;
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.package_type = package_type;
    }

    public OrderExpress(String order_time, String create_time, String order_number, String ship_name, String ship_mobile, String ship_province,
                        String ship_city, String ship_area, String ship_addr, String weight, ObjectType object_type,
                        OrderExpressState state, int sender_user_id, String order_id, String uuid, Double latitude, Double longitude,PackageType package_type) {
        this.order_time = order_time;
        this.create_time = create_time;
        this.order_number = order_number;
        this.ship_name = ship_name;
        this.ship_mobile = ship_mobile;
        this.ship_province = ship_province;
        this.ship_city = ship_city;
        this.ship_area = ship_area;
        this.ship_addr = ship_addr;
        this.weight = weight;
        this.object_type = object_type;
        this.state = state;
        this.sender_user_id = sender_user_id;
        this.order_id = order_id;
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.package_type = package_type;
    }

    public OrderExpress(String uuid, String order_id, Double longitude, Double latitude, OrderExpressState state) {
        this.state = state;
        this.order_id = order_id;
        this.uuid = uuid;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public OrderExpress(OrderExpressState state, String uuid) {
        this.state = state;
        this.uuid = uuid;
    }

    public OrderExpress(OrderExpressState state, String uuid, String attributes) {
        this.state = state;
        this.uuid = uuid;
        this.attributes = attributes;
    }

    public OrderExpress(String order_number, PackageType package_type, ObjectType object_type,
                        String order_id, String create_time, int is_use, OrderExpressState state, String uuid, int sender_user_id, String reserve_time,String weight) {
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
        this.weight = weight;
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
        this.weight = (String) request.getParameter("weight");
        this.object_type = (ObjectType)EnumUtils.enumValueOf((String) request.getParameter("object_type"),ObjectType.class);
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
    public int getIs_directed() {return is_directed;}

    public void setIs_directed(int is_directed) {
        this.is_directed = is_directed;
    }
}