package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单计价request对象")
public class PriceRequest {
    @ApiModelProperty(name = "region",example = "68034a73fccc11e68c2e0242ac1a0504",required = true)
    private String region;
    @ApiModelProperty(name = "reserve_time",example = "1501497840000",required = true)
    private String reserve_time;
    @ApiModelProperty(name = "source",required = true)
    private SourceAddressVO source;
    @ApiModelProperty(name = "target",required = true)
    private TargetAddressVO target;
    @ApiModelProperty(name = "merchant",required = true)
    private MerchantVO merchant;
    @ApiModelProperty(name = "attributes",required = true)
    private AttributesVO attributes;
    @ApiModelProperty(name = "packages",required = true)
    private List<Packages> packages;
    @ApiModelProperty(name = "product_type",example = "JISUDA",required = true)
    private String product_type;
    @ApiModelProperty(name = "pay_type",example = "FREIGHT_COLLECT",required = true)
    private String pay_type;
    @ApiModelProperty(name = "token",required = true)
    private TokenVO token;

    public String getRegion() {return region;}

    public void setRegion(String region) {this.region = region;}

    public SourceAddressVO getSource() {return source;}

    public void setSource(SourceAddressVO source) {this.source = source;}

    public TargetAddressVO getTarget() {return target;}

    public void setTarget(TargetAddressVO target) {this.target = target;}

    public MerchantVO getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantVO merchant) {
        this.merchant = merchant;
    }

    public AttributesVO getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesVO attributes) {
        this.attributes = attributes;
    }

    public String getReserve_time() {return reserve_time;}

    public void setReserve_time(String reserve_time) {this.reserve_time = reserve_time;}

    public TokenVO getToken() {return token;}

    public void setToken(TokenVO token) {this.token = token;}

    public List<Packages> getPackages() {return packages;}

    public void setPackages(List<Packages> packages) {this.packages = packages;}

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }
}
