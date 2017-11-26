package com.sftc.web.service;


import com.sftc.tools.api.APIResponse;

import com.sftc.tools.api.APIRequest;
import com.sftc.web.model.entity.Address;


public interface AddressService {

    /**
     * 地址解析(地址转坐标)
     */
    APIResponse geocoderAddress(APIRequest request);

    /**
     * 距离计算
     */
    APIResponse getAddressDistance(APIRequest request);

}
