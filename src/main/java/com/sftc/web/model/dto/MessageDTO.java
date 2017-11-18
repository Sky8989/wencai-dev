package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@ApiModel(value = "消息通知包装类")
public class MessageDTO extends Message {

    @Getter
    @Setter
    @ApiModelProperty(name = "order",value = "订单信息")
    private OrderDTO order;
}
