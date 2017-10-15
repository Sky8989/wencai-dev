package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Message;

public class MessageDTO extends Message {

    private OrderDTO order;

    public OrderDTO getOrderDTO() {return order;}

    public void setOrderDTO(OrderDTO orderDTO) {this.order = orderDTO;}

//    public Order getOrder() {
//        return order;
//    }
//
//    public void setOrder(Order order) {
//        this.order = order;
//    }

    public MessageDTO() {
        super();
    }
}
