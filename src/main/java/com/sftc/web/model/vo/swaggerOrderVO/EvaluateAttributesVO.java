package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单评价的内容")
public class EvaluateAttributesVO {
    @ApiModelProperty(name = "merchant_comments",value = "评价内容",example = "测试评价内容")
   private String merchant_comments;
    @ApiModelProperty(name = "merchant_score",value = "评价标签",example = "5")
   private String merchant_score;
    @ApiModelProperty(name = "merchant_tags",value = "评价星级",example = "服务好评,速度超快")
   private String merchant_tags;

    public String getMerchant_comments() {
        return merchant_comments;
    }

    public void setMerchant_comments(String merchant_comments) {
        this.merchant_comments = merchant_comments;
    }

    public String getMerchant_score() {
        return merchant_score;
    }

    public void setMerchant_score(String merchant_score) {
        this.merchant_score = merchant_score;
    }

    public String getMerchant_tags() {
        return merchant_tags;
    }

    public void setMerchant_tags(String merchant_tags) {
        this.merchant_tags = merchant_tags;
    }
}
