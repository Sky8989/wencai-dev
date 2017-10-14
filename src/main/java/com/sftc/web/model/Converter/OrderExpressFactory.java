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
}
