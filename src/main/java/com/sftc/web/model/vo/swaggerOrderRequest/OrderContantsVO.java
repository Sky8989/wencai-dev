package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xf on 2017/10/21.
 */
@ApiModel(value = "订单时间规则包装类")
public class OrderContantsVO extends BaseVO {

    @Getter @Setter
    @ApiModelProperty(name = "longitude",value = "经度",example = "113.947074",required = true,dataType = "double")
    private double longitude;

    @Getter @Setter
    @ApiModelProperty(name = "latitude",value = "纬度",example = "22.530375",required = true,dataType = "double")
    private double latitude;
    @Getter @Setter

    @ApiModelProperty(name = "constants",value = "常量",example = "BASICDATA",required = true)
    private String constants;
}
