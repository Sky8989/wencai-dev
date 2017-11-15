package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "大网寄件人地址包装类")
public class OrderNationSfVO {

    @ApiModelProperty(name = "j_contact", example = "寄件测试订单请勿", required = true)
    private String j_contact;
    @ApiModelProperty(name = "j_tel", example = "18666228951", required = true)
    private String j_tel;
    @ApiModelProperty(name = "j_mobile", example = "18666228951", required = true)
    private String j_mobile;
    @ApiModelProperty(name = "j_country", example = "中国", required = true)
    private String j_country;
    @ApiModelProperty(name = "j_province", example = "广东", required = true)
    private String j_province;
    @ApiModelProperty(name = "j_city", example = "深圳", required = true)
    private String j_city;
    @ApiModelProperty(name = "j_county", example = "南山区", required = true)
    private String j_county;
    @ApiModelProperty(name = "j_address", example = "广东省深圳市南山区软件产业基地1栋C座", required = true)
    private String j_address;
    @ApiModelProperty(name = "j_supplementary_info", example = "10层10号办公室", required = true)
    private String j_supplementary_info;
    @ApiModelProperty(name = "d_contact", example = "测试订单请勿", required = true)
    private String d_contact;
    @ApiModelProperty(name = "d_tel", example = "18666228952", required = true)
    private String d_tel;
    @ApiModelProperty(name = "d_mobile", example = "18666228952", required = true)
    private String d_mobile;
    @ApiModelProperty(name = "d_country", example = "中国", required = true)
    private String d_country;
    @ApiModelProperty(name = "d_province", example = "广东省", required = true)
    private String d_province;
    @ApiModelProperty(name = "d_city", example = "广州市", required = true)
    private String d_city;
    @ApiModelProperty(name = "d_county", example = "越秀区", required = true)
    private String d_county;
    @ApiModelProperty(name = "d_address", example = "广东省广州市越秀区小北地铁站", required = true)
    private String d_address;
    @ApiModelProperty(name = "d_supplementary_info", example = "20层22号办公室", required = true)
    private String d_supplementary_info;
    @ApiModelProperty(name = "pay_method", example = "FREIGHT_PREPAID", required = true)
    private String pay_method;
    @ApiModelProperty(name = "express_type", example = "2", required = true)
    private String express_type;
    @ApiModelProperty(name = "remark", example = "【神秘件】", required = true)
    private String remark;

    public String getJ_contact() {
        return j_contact;
    }

    public void setJ_contact(String j_contact) {
        this.j_contact = j_contact;
    }

    public String getJ_tel() {
        return j_tel;
    }

    public void setJ_tel(String j_tel) {
        this.j_tel = j_tel;
    }

    public String getJ_mobile() {
        return j_mobile;
    }

    public void setJ_mobile(String j_mobile) {
        this.j_mobile = j_mobile;
    }

    public String getJ_country() {
        return j_country;
    }

    public void setJ_country(String j_country) {
        this.j_country = j_country;
    }

    public String getJ_province() {
        return j_province;
    }

    public void setJ_province(String j_province) {
        this.j_province = j_province;
    }

    public String getJ_city() {
        return j_city;
    }

    public void setJ_city(String j_city) {
        this.j_city = j_city;
    }

    public String getJ_county() {
        return j_county;
    }

    public void setJ_county(String j_county) {
        this.j_county = j_county;
    }

    public String getJ_address() {
        return j_address;
    }

    public void setJ_address(String j_address) {
        this.j_address = j_address;
    }

    public String getJ_supplementary_info() {
        return j_supplementary_info;
    }

    public void setJ_supplementary_info(String j_supplementary_info) {
        this.j_supplementary_info = j_supplementary_info;
    }

    public String getD_contact() {
        return d_contact;
    }

    public void setD_contact(String d_contact) {
        this.d_contact = d_contact;
    }

    public String getD_tel() {
        return d_tel;
    }

    public void setD_tel(String d_tel) {
        this.d_tel = d_tel;
    }

    public String getD_mobile() {
        return d_mobile;
    }

    public void setD_mobile(String d_mobile) {
        this.d_mobile = d_mobile;
    }

    public String getD_country() {
        return d_country;
    }

    public void setD_country(String d_country) {
        this.d_country = d_country;
    }

    public String getD_province() {
        return d_province;
    }

    public void setD_province(String d_province) {
        this.d_province = d_province;
    }

    public String getD_city() {
        return d_city;
    }

    public void setD_city(String d_city) {
        this.d_city = d_city;
    }

    public String getD_county() {
        return d_county;
    }

    public void setD_county(String d_county) {
        this.d_county = d_county;
    }

    public String getD_address() {
        return d_address;
    }

    public void setD_address(String d_address) {
        this.d_address = d_address;
    }

    public String getD_supplementary_info() {
        return d_supplementary_info;
    }

    public void setD_supplementary_info(String d_supplementary_info) {
        this.d_supplementary_info = d_supplementary_info;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getExpress_type() {
        return express_type;
    }

    public void setExpress_type(String express_type) {
        this.express_type = express_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
