package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerOrderRequest.MultiplePackagePayVO;
import com.sftc.web.model.vo.swaggerOrderRequest.MultiplePackageVO;

/**
 * 好友多包裹
 *
 * @author ： CatalpaFlat
 */
public interface MultiplePackageService {
    ApiResponse batchValuation(MultiplePackageVO request);

    ApiResponse batchPlaceOrder(MultiplePackageVO body);

    ApiResponse batchPay(MultiplePackagePayVO body);

    ApiResponse isPay(MultiplePackagePayVO body);
}
