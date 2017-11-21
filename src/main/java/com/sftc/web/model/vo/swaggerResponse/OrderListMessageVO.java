package com.sftc.web.model.vo.swaggerResponse;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xf on 2017/11/17.
 */
@ApiModel(value = "订单列表信息")
public class OrderListMessageVO extends Order{

    @Getter @Setter
    @ApiModelProperty(name = "orderExpressList",value = "快递数组")
    public List<OrderListExpressVO> orderExpressList;  // 快递数组

}
