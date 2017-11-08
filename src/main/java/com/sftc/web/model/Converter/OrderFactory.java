package com.sftc.web.model.Converter;

import com.sftc.web.model.dto.AddressDTO;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.Address;
import com.sftc.web.model.entity.Order;

/**
 * Created by xf on 2017/10/1.
 */
public class OrderFactory {
    public static Order dtoToEntity(OrderDTO orderDTO){
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setCreate_time(orderDTO.getCreate_time());
        order.setPay_method(orderDTO.getPay_method());
        order.setOrder_type(orderDTO.getOrder_type());
        order.setOrder_type(orderDTO.getOrder_type());
        order.setRegion_type(orderDTO.getRegion_type());
        order.setSender_user_id(orderDTO.getSender_user_id());
        order.setSender_province(orderDTO.getSender_province());
        order.setSender_name(orderDTO.getSender_name());
        order.setSender_mobile(orderDTO.getSender_mobile());
        order.setSender_city(orderDTO.getSender_city());
        order.setSender_area(orderDTO.getSender_area());
        order.setSender_addr(orderDTO.getSender_addr());
        order.setSupplementary_info(orderDTO.getSupplementary_info());
        order.setGift_card_id(orderDTO.getGift_card_id());
        order.setWord_message(orderDTO.getWord_message());
        order.setImage(orderDTO.getImage());
        order.setVoice(orderDTO.getVoice());
        order.setVoice_time(orderDTO.getVoice_time());
        order.setPay_method(orderDTO.getPay_method());
        order.setDistribution_method(orderDTO.getDistribution_method());
        order.setLongitude(orderDTO.getLongitude());
        order.setLatitude(orderDTO.getLatitude());
        order.setIs_cancel(orderDTO.getIs_cancel());
        return  order;
    }
}
