package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "通过城市 查询价格说明")
public class PriceExplainVO extends BaseVO {
    @ApiModelProperty(name = "city", value = "城市",example = "北京",required = true)
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
