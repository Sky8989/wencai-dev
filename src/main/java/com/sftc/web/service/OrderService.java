package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service
 * @Description: 订单操作接口
 * @date 17/4/1
 * @Time 下午9:32
 */

public interface OrderService {

    /**
     * 下单
     * @param request
     * @return
     */
    APIResponse placeOrder(APIRequest request);
}
