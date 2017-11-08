package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Message;

public class MessageDTO extends Message {

    private OrderDTO order;

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public MessageDTO() {
        super();
    }
}
