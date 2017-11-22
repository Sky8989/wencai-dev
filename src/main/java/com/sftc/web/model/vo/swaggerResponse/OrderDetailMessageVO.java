package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.web.model.entity.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "订单详情快递信息")
public class OrderDetailMessageVO extends Order{

    @Getter @Setter
    @ApiModelProperty(name = "orderExpressList",value = "快递数组")
    public List<OrderDetailExpressVO> orderExpressList;  // 快递数组

}
