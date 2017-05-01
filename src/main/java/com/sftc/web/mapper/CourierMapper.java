package com.sftc.web.mapper;

import com.sftc.web.model.Courier;

public interface CourierMapper {
    Courier selectCourierInformation(String courierSn);
}
