package com.sftc.web.service;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.PriceExplain;
import com.sftc.web.model.vo.swaggerRequestVO.priceExaplain.DeletePriceExplain;

public interface PriceExaplainService {
    APIResponse getPriceExplainByCity(APIRequest apiRequest);

	APIResponse addPriceExplain(PriceExplain priceExplain);

	APIResponse updatePriceExplain(PriceExplain priceExplain);

	APIResponse save(APIRequest apiRequest);

	APIResponse deletePriceExplain(APIRequest apiRequest);

	APIResponse findPriceExplainList();


}
