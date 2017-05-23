package com.sftc.web.service;


import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.sfmodel.Requests;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Address;


/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service
 * @Description: 地址操作接口
 * @date 17/4/1
 * @Time 下午9:30
 */
public interface AddressService {

    /**
     * 新增地址
     * @param @request
     * @return
     */
    APIResponse addAddress(Object object);


    /**
     * 新增新地址
     * @param address
     * @return
     */
    APIResponse addAddress(Address address);

    /**
     * 我的收件人地址
     * @param request
     * @return
     */
    APIResponse consigneeAddress(APIRequest request);

    /**
     * 修改收件人地址
     * @param address
     * @return
     */
    APIResponse editAddress(Address address);

    /**
     * 删除收件人地址
     * @param request
     * @return
     */
    APIResponse deleteAddress(APIRequest request);

}
