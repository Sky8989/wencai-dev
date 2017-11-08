package com.sftc.web.model.SwaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友订单填写包装类")
public class FriendFillVO {
    @ApiModelProperty(name = "ship_name",value = "收件人姓名",example = "庄槟豪测试订单")
    private String ship_name;
    @ApiModelProperty(name = "ship_mobile",value = "收件人电话",example = "13544185508")
    private String ship_mobile;
    @ApiModelProperty(name = "ship_province",value = "收件人省份",example = "广东省")
    private String ship_province;
    @ApiModelProperty(name = "ship_city",value = "收件人城市",example = "深圳市")
    private String ship_city;
    @ApiModelProperty(name = "ship_area",value = "收件人区域",example = "龙岗区")
    private String ship_area;
    @ApiModelProperty(name = "ship_addr",value = "收件人详细地址",example = "龙翔大道2188号")
    private String ship_addr;
    @ApiModelProperty(name = "supplementary_info",value = "收件人门牌号",example = "好友填写3号")
    private String supplementary_info;
    @ApiModelProperty(name = "order_id",value = "订单id",example = "C1508415669414OY")
    private String order_id;
    @ApiModelProperty(name = "ship_user_id",value = "收件人id",example = "10087")
    private String ship_user_id;
    @ApiModelProperty(name = "longitude",value = "经度",example = "113.9466987556842")
    private String longitude;
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.530164470515828")
    private String latitude;

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

    public String getSupplementary_info() {
        return supplementary_info;
    }

    public void setSupplementary_info(String supplementary_info) {
        this.supplementary_info = supplementary_info;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getShip_user_id() {
        return ship_user_id;
    }

    public void setShip_user_id(String ship_user_id) {
        this.ship_user_id = ship_user_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
