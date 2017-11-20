package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

/**
 * @author ： CatalpaFlat
 * @date ：Create in 14:13 2017/11/17
 */
public interface MultiplePackageService {
    APIResponse batchValuation(APIRequest request);

    APIResponse batchPlaceOrder(APIRequest request);
}
