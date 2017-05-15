package com.sftc.web.mapper;

import com.sftc.web.model.Courier;

import java.util.Map;

public interface CourierMapper {
    Courier selectCourierInformation(Map<String,String> param);
    Courier getCourier(String express_number);
}
