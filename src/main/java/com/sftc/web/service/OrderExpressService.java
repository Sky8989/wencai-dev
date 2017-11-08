package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description: 快递订单操作接口
 * @date 2017/5/9
 * @Time 下午5:01
 */
public interface OrderExpressService {
    //cms 获取快递信息列表 分页+条件
    APIResponse selectOrderExpressListByPage(APIRequest apiRequest);


    /**
     * 查询已经大网转单的订单
     * @param apiRequest 通用请求
     * @return APIResponse
     */
    APIResponse selectOrderExpressTransformList(APIRequest apiRequest);
}
