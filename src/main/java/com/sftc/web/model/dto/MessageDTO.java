package com.sftc.web.model.dto;

import com.sftc.web.model.entity.Message;
import lombok.Getter;
import lombok.Setter;

public class MessageDTO extends Message {

    @Getter
    @Setter
    private OrderDTO order;
}
