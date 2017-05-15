package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.OrderCancel;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service
 * @Description: 取消订单操作接口
 * @date 17/4/1
 * @Time 下午9:30
 */
public interface OrderCancelService {
    APIResponse deleteOrder(OrderCancel orderCancel);
}
