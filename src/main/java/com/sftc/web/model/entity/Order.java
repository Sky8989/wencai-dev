package com.sftc.web.model.entity;

import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.web.model.others.Object;
import com.sftc.web.model.vo.swaggerOrderVO.OrderParamVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

@Entity
@Table(name = "sftc_order")
@ApiModel(value = "订单实体类")
public class Order extends Object {
    @Id
    @Setter @Getter
    @ApiModelProperty(name = "id",value = "订单id",example = "C1510544211749T8")
    private String id;

    @Setter @Getter
    @ApiModelProperty(name = "id",value = "创建时间",example = "1510544211749")
    private String create_time;// 创建时间

    @Setter @Getter
    @ApiModelProperty(name = "pay_time",value = "支付时间",example = "1510544211749")
    private String pay_time;// 支付时间

    @Setter @Getter
    @ApiModelProperty(name = "pay_method",value = "付款方式",example = "FREIGHT_COLLECT")
    private String pay_method; // 付款方式

    @Setter @Getter
    @ApiModelProperty(name = "distribution_method",value = "配送方式",example = "KUAISUDA")
    private String distribution_method;// 配送方式

    @Setter @Getter
    @ApiModelProperty(name = "sender_name",value = "寄件人姓名",example = "123测试订单")
    private String sender_name;// 寄件人姓名

    @Setter @Getter
    @ApiModelProperty(name = "sender_mobile",value = "寄件人手机",example = "13691698530")
    private String sender_mobile;// 寄件人手机

    @Setter @Getter
    @ApiModelProperty(name = "sender_province",value = "寄件人省",example = "广东省")
    private String sender_province; // 寄件人省

    @Setter @Getter
    @ApiModelProperty(name = "sender_city",value = "寄件人市",example = "深圳市")
    private String sender_city;// 寄件人市

    @Setter @Getter
    @ApiModelProperty(name = "sender_area",value = "寄件人区域",example = "龙岗区")
    private String sender_area;// 寄件人区域

    @Setter @Getter
    @ApiModelProperty(name = "sender_addr",value = "寄件人详细地址",example = "龙城广场地铁站")
    private String sender_addr;// 寄件人详细地址

    @Setter @Getter
    @ApiModelProperty(name = "supplementary_info",value = "寄件人门牌号",example = "118号")
    private String supplementary_info;// 寄件人门牌号

    @Setter @Getter
    @ApiModelProperty(name = "word_message",value = "留言",example = "一句留言")
    private String word_message;

    @Setter @Getter
    @ApiModelProperty(name = "image",value = "图片",example = "一张图片")
    private String image;

    @Setter @Getter
    @ApiModelProperty(name = "voice",value = "语音",example = "一条语音")
    private String voice;

    @Setter @Getter
    @ApiModelProperty(name = "longitude",value = "经度",example = "22.54874796811195",dataType = "double")
    private double longitude;// 经度

    @Setter @Getter
    @ApiModelProperty(name = "latitude",value = "纬度",example = "114.06441301610272",dataType = "double")
    private double latitude;// 纬度

    @Setter @Getter
    @ApiModelProperty(name = "sender_user_id",value = "寄件人id",example = "10093",dataType = "int")
    private int sender_user_id;

    @Setter @Getter
    @ApiModelProperty(name = "gift_card_id",value = "礼品卡id",example = "17",dataType = "int")
    private int gift_card_id;

    @Setter @Getter
    @ApiModelProperty(name = "voice_time",value = "语音时长",example = "10",dataType = "int")
    private int voice_time;

    @Setter @Getter
    @ApiModelProperty(name = "order_type",value = "订单类型",example = "ORDER_BASIS")
    private String order_type; // 订单类型 普通 神秘

    @Setter @Getter
    @ApiModelProperty(name = "region_type",value = "订单地域 同城",example = "REGION_SAME")
    private String region_type;// 订单地域 同城

    @ApiModelProperty(name = "is_cancel",value = "是否取消",example = "0",dataType = "int")
    private int is_cancel;//新添加 is_cancel

    public Order() {
    }

    public Order(double longitude, double latitude, String id) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Order(String create_time, String id,String pay_method,
                 String distribution_method, String sender_name, String sender_mobile, String sender_province,
                 String sender_city, String sender_area, String sender_addr,String supplementary_info,
                 double longitude, double latitude, String order_type, int sender_user_id) {
        this.create_time = create_time;
        this.id = id;
        this.pay_method = pay_method;
        this.distribution_method = distribution_method;
        this.sender_name = sender_name;
        this.sender_mobile = sender_mobile;
        this.sender_province = sender_province;
        this.sender_city = sender_city;
        this.sender_area = sender_area;
        this.sender_addr = sender_addr;
        this.supplementary_info = supplementary_info;
        this.longitude = longitude;
        this.latitude = latitude;
        this.order_type = order_type;
        this.sender_user_id = sender_user_id;
    }
    public Order(String create_time, String pay_method,
                 String distribution_method, String sender_name, String sender_mobile, String sender_province,
                 String sender_city, String sender_area, String sender_addr,
                 double longitude, double latitude, String order_type, int sender_user_id) {
        this.create_time = create_time;
        this.pay_method = pay_method;
        this.distribution_method = distribution_method;
        this.sender_name = sender_name;
        this.sender_mobile = sender_mobile;
        this.sender_province = sender_province;
        this.sender_city = sender_city;
        this.sender_area = sender_area;
        this.sender_addr = sender_addr;
        this.longitude = longitude;
        this.latitude = latitude;
        this.order_type = order_type;
        this.sender_user_id = sender_user_id;
    }

    public Order(OrderParamVO orderParamVO) {
        this.create_time = Long.toString(System.currentTimeMillis());
        this.id = SFOrderHelper.getOrderId();
        this.pay_method = orderParamVO.getPay_method();
        this.distribution_method = orderParamVO.getDistribution_method();
        this.sender_name = orderParamVO.getSender_name();
        this.sender_mobile = orderParamVO.getSender_mobile();
        this.sender_province = orderParamVO.getSender_province();
        this.sender_city = orderParamVO.getSender_city();
        this.sender_area = orderParamVO.getSender_area();
        this.sender_addr = orderParamVO.getSender_addr();
        this.supplementary_info = orderParamVO.getSupplementary_info();
        this.word_message = orderParamVO.getWord_message();
        this.image = orderParamVO.getImage();
        this.voice = orderParamVO.getVoice();
        this.longitude = orderParamVO.getLongitude();
        this.latitude = orderParamVO.getLatitude();
        this.voice_time = orderParamVO.getVoice_time();
        this.sender_user_id = orderParamVO.getSender_user_id();
        this.gift_card_id = orderParamVO.getGift_card_id();
        this.order_type = orderParamVO.getOrder_type();
        this.region_type = orderParamVO.getRegion_type();
    }

    /**
     * 基于HttpServletRequest作为参数的构造方法 用于cms
     * 后期便于应用扩展工厂模式 将此参数抽出
     */
    public Order(HttpServletRequest request) {
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
            this.id = request.getParameter("id");
        }
        if (request.getParameter("order_type") != null && !"".equals(request.getParameter("order_type"))) {
            this.order_type = request.getParameter("order_type");
        }
        if (request.getParameter("region_type") != null && !"".equals(request.getParameter("region_type"))) {
            this.region_type = request.getParameter("region_type");
        }
        if (request.getParameter("sender_mobile") != null && !"".equals(request.getParameter("sender_mobile"))) {
            this.sender_mobile = request.getParameter("sender_mobile");
        }
    }

    public int getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(int is_cancel) {
        this.is_cancel = is_cancel;
    }
}
