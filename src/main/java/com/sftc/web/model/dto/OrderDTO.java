package com.sftc.web.model.dto;

import com.sftc.web.model.Evaluate;
import com.sftc.web.model.GiftCard;
import com.sftc.web.model.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class OrderDTO extends Order {

    @Getter
    @Setter
    private GiftCard giftCard;   // 贺卡

    @Getter
    @Setter
    private Evaluate evaluate;   // 订单评价信息

    @Getter
    @Setter
    public List<OrderExpressDTO> orderExpressList;  // 快递数组
}
