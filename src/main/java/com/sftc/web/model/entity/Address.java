package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import com.sftc.web.model.vo.BaseVO;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderParamVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "c_address")
@ApiModel(value = "地址")
public class Address extends BaseVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键",hidden = true)
    @Setter @Getter
    private Integer id;

    @ApiModelProperty(value = "用户uudi",hidden = true)
    @Setter @Getter
    private String user_uuid;

    @ApiModelProperty(value = "地址名称",example = "悟空测试订单",required = true)
    @Setter @Getter
    private String name;

    @ApiModelProperty(value = "用户电话",example = "13066667777",required = true)
    @Setter @Getter
    private String phone;

    @ApiModelProperty(value = "省份",example = "广东",required = true)
    @Setter @Getter
    private String province;

    @ApiModelProperty(value = "城市",example = "深圳",required = true)
    @Setter @Getter
    private String city;

    @ApiModelProperty(value = "区域",example = "龙岗区",required = true)
    @Setter @Getter
    private String area;

    @ApiModelProperty(value = "详细地址",example = "龙城广场地铁站",required = true)
    @Setter @Getter
    private String address;

    @ApiModelProperty(value = "补充地址",example = "118号")
    @Setter @Getter
    private String supplementary_info;

    @ApiModelProperty(value = "经度",example = "114.260976",required = true,dataType = "double")
    @Setter @Getter
    private Double longitude;

    @ApiModelProperty(value = "纬度",example = "22.723223",required = true,dataType = "double")
    @Setter @Getter
    private Double latitude;

    @ApiModelProperty(value = "创建时间",hidden = true)
    @Setter @Getter
    private String create_time;

    public Address(){}

    public Address(Integer id, String user_uuid, String name, String phone, String province, String city, String area, String address, String create_time) {
        this.id = id;
        this.user_uuid = user_uuid;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
        this.create_time = create_time;
    }

    public Address(OrderParamVO orderParamVO) {
        this.setName(orderParamVO.getSender_name());
        this.setPhone(orderParamVO.getSender_mobile());
        this.setProvince(orderParamVO.getSender_province());
        this.setCity(orderParamVO.getSender_city());
        this.setArea(orderParamVO.getSender_area());
        this.setAddress(orderParamVO.getSender_addr());
        this.setArea(orderParamVO.getSender_area());
        this.setSupplementary_info(orderParamVO.getSupplementary_info());
        this.setLongitude(orderParamVO.getLongitude());
        this.setLatitude(orderParamVO.getLatitude());
        this.setCreate_time(Long.toString(System.currentTimeMillis()));
    }

    public Address(OrderExpress oe) {
        this.setName(oe.getShip_name());
        this.setPhone(oe.getShip_mobile());
        this.setProvince(oe.getShip_province());
        this.setCity(oe.getShip_city());
        this.setArea(oe.getShip_area());
        this.setAddress(oe.getShip_addr());
        this.setArea(oe.getShip_area());
        this.setLongitude(oe.getLongitude());
        this.setLatitude(oe.getLatitude());
        this.setCreate_time(Long.toString(System.currentTimeMillis()));
    }

    public Address(String user_uuid, String name, String phone, String province, String city, String area, String address, String supplementary_info, Double longitude, Double latitude, String create_time) {
        this.user_uuid = user_uuid;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
        this.supplementary_info = supplementary_info;
        this.longitude = longitude;
        this.latitude = latitude;
        this.create_time = create_time;
    }
}
