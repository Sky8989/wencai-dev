package com.sftc.web.model.SwaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "好友大网单快递信息")
public class FriendNationSFVO {
    @ApiModelProperty(name = "pay_method",example = "FREIGHT_PREPAID",required = true)
    private String pay_method;
    @ApiModelProperty(name = "express_type",example = "2",required = true)
    private String express_type;
    @ApiModelProperty(name = "remark",example = "【神秘件】",required = true)
    private String remark;

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
