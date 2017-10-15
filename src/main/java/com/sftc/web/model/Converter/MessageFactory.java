package com.sftc.web.model.Converter;

import com.sftc.web.model.dto.MessageDTO;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.Message;
import com.sftc.web.model.entity.Order;

/**
 * Created by xf on 2017/10/1.
 */
public class MessageFactory {
    public static MessageDTO entityToDTO(Message message){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setIs_read(message.getIs_read());
        messageDTO.setExpress_id(message.getExpress_id());
        messageDTO.setCreate_time(message.getCreate_time());
        messageDTO.setId(message.getId());
        messageDTO.setUser_id(message.getUser_id());
        messageDTO.setMessage_type(message.getMessage_type());
        return  messageDTO;
    }
}
