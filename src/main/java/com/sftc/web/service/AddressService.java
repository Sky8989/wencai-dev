package com.sftc.web.service;


import com.sftc.tools.api.ApiResponse;

import com.sftc.tools.api.ApiRequest;
import com.sftc.web.model.entity.Address;
import com.sftc.web.model.vo.swaggerRequest.DistanceRequestVO;

/**
 * 
 * @author Administrator
 *
 */
public interface AddressService {

    /**
     * 新增地址
     */
    ApiResponse addAddress(Address body);


    /**
     * 我的收件人地址
     */
    ApiResponse consigneeAddress();

    /**
     * 修改收件人地址
     */
    ApiResponse editAddress(Address body);

    /**
     * 删除收件人地址
     */
    ApiResponse deleteAddress(ApiRequest request);

    /**
     * 地址解析(地址转坐标)
     */
    ApiResponse geocoderAddress(String address);

    /**
     * 距离计算
     */
    ApiResponse getAddressDistance(DistanceRequestVO body);

}
