package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

public interface SFServiceAddressService {

    /**
     * 更新顺丰速运服务地址
     */
    APIResponse updateServiceAddress(APIRequest request);

    /**
     * 查询动态配送时效价格
     */
    APIResponse selectDynamicPrice(APIRequest request);
}
