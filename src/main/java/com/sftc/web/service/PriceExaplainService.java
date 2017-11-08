package com.sftc.web.service;


import com.sftc.tools.api.APIResponse;

public interface PriceExaplainService {
    APIResponse getPriceExplainByCity(String city);
}
