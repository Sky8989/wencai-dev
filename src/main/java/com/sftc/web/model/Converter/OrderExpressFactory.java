package com.sftc.web.model.Converter;

import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;

/**
 * Created by xf on 2017/10/1.
 */
public class OrderExpressFactory {
    public static OrderExpressDTO entityToDTO(OrderExpress orderExpress){
        OrderExpressDTO orderExpressDTO = new OrderExpressDTO();
        orderExpressDTO.setId(orderExpress.getId());
        orderExpressDTO.setCreate_time(orderExpress.getCreate_time());
        orderExpressDTO.setOrder_number(orderExpress.getOrder_number());
        orderExpressDTO.setShip_name(orderExpress.getShip_name());
        orderExpressDTO.setShip_mobile(orderExpress.getShip_mobile());
        orderExpressDTO.setShip_province(orderExpress.getShip_province());
        orderExpressDTO.setShip_city(orderExpress.getRegion_type());
        orderExpressDTO.setSender_user_id(orderExpress.getSender_user_id());
        orderExpressDTO.setSender_province(orderExpress.getSender_province());
        orderExpressDTO.setSender_name(orderExpress.getSender_name());
        orderExpressDTO.setSender_mobile(orderExpress.getSender_mobile());
        orderExpressDTO.setSender_city(orderExpress.getSender_city());
        orderExpressDTO.setSender_area(orderExpress.getSender_area());
        orderExpressDTO.setSender_addr(orderExpress.getSender_addr());
        orderExpressDTO.setSupplementary_info(orderExpress.getSupplementary_info());
        orderExpressDTO.setGift_card_id(orderExpress.getGift_card_id());
        orderExpressDTO.setWord_message(orderExpress.getWord_message());
        orderExpressDTO.setImage(orderExpress.getImage());
        orderExpressDTO.setVoice(orderExpress.getVoice());
        orderExpressDTO.setVoice_time(orderExpress.getVoice_time());
        orderExpressDTO.setPay_method(orderExpress.getPay_method());
        orderExpressDTO.setDistribution_method(orderExpress.getDistribution_method());
        orderExpressDTO.setLongitude(orderExpress.getLongitude());
        orderExpressDTO.setLatitude(orderExpress.getLatitude());
        orderExpressDTO.setIs_cancel(orderExpress.getIs_cancel());
        return  orderExpressDTO;
    }
}
