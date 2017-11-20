package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Evaluate;
import com.sftc.web.model.entity.GiftCard;
import com.sftc.web.model.entity.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@ApiModel(value = "订单实体信息包装类")
public class OrderDTO extends Order {

    @Getter
    @Setter
    @ApiModelProperty(name = "giftCard",value = "贺卡",hidden = true)
    private GiftCard giftCard;   // 贺卡

    @Getter
    @Setter
    @ApiModelProperty(name = "evaluate",value = "订单评价信息",hidden = true)
    private Evaluate evaluate;   // 订单评价信息

    @Getter
    @Setter
    @ApiModelProperty(name = "orderExpressList",value = "快递数组")
    public List<OrderExpressDTO> orderExpressList;  // 快递数组
}
