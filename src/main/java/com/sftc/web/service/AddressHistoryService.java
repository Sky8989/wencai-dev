package com.sftc.web.service;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

public interface AddressHistoryService {

    /**
     * 查询历史地址
     */
    APIResponse selectAddressHistory(APIRequest request);
}
