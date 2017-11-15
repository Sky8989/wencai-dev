package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友同城订单request对象")
public class FriendRequestVO {
    @ApiModelProperty(name = "region",example = "68034a73fccc11e68c2e0242ac1a0504",required = true)
    private String region;
    @ApiModelProperty(name = "merchant",required = true)
    private MerchantVO merchant;
    @ApiModelProperty(name = "attributes",required = true)
    private AttributesVO attributes;
    @ApiModelProperty(name = "friend_quote",required = true)
    private FriendQuoteVO quote;
    @ApiModelProperty(name = "packages",required = true)
    private List<PackagesVO> packages;
    @ApiModelProperty(name = "product_type",example = "JISUDA",required = true)
    private String product_type;
    @ApiModelProperty(name = "pay_type",example = "FREIGHT_COLLECT",required = true)
    private String pay_type;

    public String getRegion() {return region;}

    public void setRegion(String region) {this.region = region;}

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

    public FriendQuoteVO getQuote() {return quote;}

    public void setQuote(FriendQuoteVO quote) {this.quote = quote;}

    public List<PackagesVO> getPackages() {return packages;}

    public void setPackages(List<PackagesVO> packages) {this.packages = packages;}

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
