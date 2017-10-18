package com.sftc.web.service;


import com.sftc.tools.api.APIResponse;

import com.sftc.tools.api.APIRequest;
import com.sftc.web.model.entity.Address;


public interface AddressService {

    /**
     * 新增地址
     */
    APIResponse addAddress(Object object);

    /**
     * 新增新地址
     */
    APIResponse addAddress(Address address);

    /**
     * 我的收件人地址
     */
    APIResponse consigneeAddress(APIRequest request);

    /**
     * 修改收件人地址
     */
    APIResponse editAddress(Address address);

    /**
     * 删除收件人地址
     */
    APIResponse deleteAddress(APIRequest request);

    /**
     * 地址解析(地址转坐标)
     */
    APIResponse geocoderAddress(APIRequest request);

    /**
     * 距离计算
     */
    APIResponse getAddressDistance(APIRequest request);

}
