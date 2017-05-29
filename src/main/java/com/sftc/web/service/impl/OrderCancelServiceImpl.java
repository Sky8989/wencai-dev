package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.OrderCancelMapper;
import com.sftc.web.model.OrderCancel;
import com.sftc.web.service.OrderCancelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 取消订单操作接口实现
 * @date 17/4/1
 * @Time 下午9:33
 */
@Service
public class OrderCancelServiceImpl implements OrderCancelService {
    @Resource
    OrderCancelMapper orderCancelMapper;
    public APIResponse deleteOrder(OrderCancel orderCancel) {
        APIStatus status = APIStatus.SUCCESS;
        orderCancel.setCreate_time("2017-1-1");
        try {
            orderCancelMapper.addCancelOrder(orderCancel);
        }catch(Exception e){
            status = APIStatus.CANCEL_ORDER_FALT;
        }
        return  APIUtil.getResponse(status, null);
    }
}
