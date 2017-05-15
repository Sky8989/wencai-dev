package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

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
     * 普通提交订单
     * @param request
     * @return
     */
    APIResponse placeOrder(APIRequest request);

    /**
     * 支付订单
     * @param request
     * @return
     */
    APIResponse payOrder(APIRequest request);

    /**
     * 好友寄件提交订单
     * @param request
     * @return
     */
    APIResponse friendPlaceOrder(APIRequest request);

    /**
     * 好友填写寄件订单
     * @param request
     * @return
     */
    APIResponse friendFillOrder(APIRequest request);

    /**
     * 返回未被填写的包裹
     * @param request
     * @return
     */
    APIResponse getEmptyPackage(APIRequest request);
}
