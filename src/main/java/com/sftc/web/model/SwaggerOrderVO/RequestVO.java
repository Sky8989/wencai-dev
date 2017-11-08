package com.sftc.web.model.SwaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "同城订单request对象")
public class RequestVO {
    @ApiModelProperty(name = "region",example = "68034a73fccc11e68c2e0242ac1a0504",required = true)
    private String region;
    @ApiModelProperty(name = "source",required = true)
    private SourceAddressVO source;
    @ApiModelProperty(name = "target",required = true)
    private TargetAddressVO target;
    @ApiModelProperty(name = "merchant",required = true)
    private MerchantVO merchant;
    @ApiModelProperty(name = "attributes",required = true)
    private AttributesVO attributes;
    @ApiModelProperty(name = "quote",required = true)
    private Quote quote;
    @ApiModelProperty(name = "packages",required = true)
    private List<Packages> packages;
    @ApiModelProperty(name = "product_type",example = "JISUDA",required = true)
    private String product_type;
    @ApiModelProperty(name = "pay_type",example = "FREIGHT_COLLECT",required = true)
    private String pay_type;

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

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

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
