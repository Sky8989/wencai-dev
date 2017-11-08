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
        orderExpressDTO.setShip_city(orderExpress.getShip_city());
        orderExpressDTO.setShip_area(orderExpress.getShip_area());
        orderExpressDTO.setShip_addr(orderExpress.getShip_addr());
        orderExpressDTO.setSender_user_id(orderExpress.getSender_user_id());
        orderExpressDTO.setSupplementary_info(orderExpress.getSupplementary_info());
        orderExpressDTO.setPackage_type(orderExpress.getPackage_type());
        orderExpressDTO.setObject_type(orderExpress.getObject_type());
        orderExpressDTO.setPackage_comments(orderExpress.getPackage_comments());
        orderExpressDTO.setIs_use(orderExpress.getIs_use());
        orderExpressDTO.setShip_user_id(orderExpress.getShip_user_id());
        orderExpressDTO.setOrder_id(orderExpress.getOrder_id());
        orderExpressDTO.setState(orderExpress.getState());
        orderExpressDTO.setUuid(orderExpress.getUuid());
        orderExpressDTO.setLatitude(orderExpress.getLatitude());
        orderExpressDTO.setLongitude(orderExpress.getLongitude());
        orderExpressDTO.setReceive_time(orderExpress.getReceive_time());
        orderExpressDTO.setReserve_time(orderExpress.getReserve_time());
        orderExpressDTO.setDirected_code(orderExpress.getDirected_code());
        orderExpressDTO.setOrder_time(orderExpress.getOrder_time());
        orderExpressDTO.setAttributes(orderExpress.getAttributes());
        return  orderExpressDTO;
    }
    public static OrderExpress dtoToEntity(OrderExpressDTO orderExpressDTO){
        OrderExpress orderExpress = new OrderExpress();
        orderExpress.setId(orderExpressDTO.getId());
        orderExpress.setCreate_time(orderExpressDTO.getCreate_time());
        orderExpress.setOrder_number(orderExpressDTO.getOrder_number());
        orderExpress.setShip_name(orderExpressDTO.getShip_name());
        orderExpress.setShip_mobile(orderExpressDTO.getShip_mobile());
        orderExpress.setShip_province(orderExpressDTO.getShip_province());
        orderExpress.setShip_city(orderExpressDTO.getShip_city());
        orderExpress.setShip_area(orderExpressDTO.getShip_area());
        orderExpress.setShip_addr(orderExpressDTO.getShip_addr());
        orderExpress.setSender_user_id(orderExpressDTO.getSender_user_id());
        orderExpress.setSupplementary_info(orderExpressDTO.getSupplementary_info());
        orderExpress.setPackage_type(orderExpressDTO.getPackage_type());
        orderExpress.setObject_type(orderExpressDTO.getObject_type());
        orderExpress.setPackage_comments(orderExpressDTO.getPackage_comments());
        orderExpress.setIs_use(orderExpressDTO.getIs_use());
        orderExpress.setShip_user_id(orderExpressDTO.getShip_user_id());
        orderExpress.setOrder_id(orderExpressDTO.getOrder_id());
        orderExpress.setState(orderExpressDTO.getState());
        orderExpress.setUuid(orderExpressDTO.getUuid());
        orderExpress.setLatitude(orderExpressDTO.getLatitude());
        orderExpress.setLongitude(orderExpressDTO.getLongitude());
        orderExpress.setReceive_time(orderExpressDTO.getReceive_time());
        orderExpress.setReserve_time(orderExpressDTO.getReserve_time());
        orderExpress.setDirected_code(orderExpressDTO.getDirected_code());
        orderExpress.setOrder_time(orderExpressDTO.getOrder_time());
        orderExpress.setAttributes(orderExpressDTO.getAttributes());
        return  orderExpress;
    }
}
