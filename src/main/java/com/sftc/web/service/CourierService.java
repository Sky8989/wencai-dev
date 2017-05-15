package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Courier;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description: 快递员操作接口
 * @date 2017/4/25
 * @Time 上午10:58
 */
public interface CourierService {
    APIResponse getCourier(APIRequest request);
}
