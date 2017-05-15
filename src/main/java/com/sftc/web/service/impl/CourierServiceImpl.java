package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.CourierMapper;
import com.sftc.web.model.Courier;
import com.sftc.web.service.CourierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service.impl
 * @Description: 快递员操作接口实现
 * @date 2017/4/25
 * @Time 上午10:58
 */
@Service
public class CourierServiceImpl implements CourierService {
    @Resource
    CourierMapper courierMapper;
    @Override
    public APIResponse getCourier(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String express_number=(String)request.getParameter("express_number");
        Courier courier = courierMapper.getCourier(express_number);
        if (courier == null){
            status = APIStatus.ORDER_NOT_FOUND;
        }
        return  APIUtil.getResponse(status, courier);
    }
}
