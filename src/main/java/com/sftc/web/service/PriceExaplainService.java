package com.sftc.web.service;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

public interface PriceExaplainService {
    APIResponse getPriceExplainByCity(APIRequest apiRequest);
}
