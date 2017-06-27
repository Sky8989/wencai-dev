package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.OrderCancelMapper;
import com.sftc.web.model.OrderCancel;
import com.sftc.web.service.OrderCancelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderCancelServiceImpl implements OrderCancelService {
    @Resource
    private OrderCancelMapper orderCancelMapper;

    public APIResponse deleteOrder(OrderCancel orderCancel) {
        APIStatus status = APIStatus.SUCCESS;
        orderCancel.setCreate_time("2017-1-1");
        try {
            orderCancelMapper.addCancelOrder(orderCancel);
        } catch (Exception e) {
            status = APIStatus.CANCEL_ORDER_FALT;
        }
        return APIUtil.getResponse(status, null);
    }
}
