package com.sftc.web.service;


import com.sftc.tools.api.APIResponse;

import javax.servlet.http.HttpServletRequest;

public interface UserUnpackingService {
    APIResponse unpacking(String order_id, HttpServletRequest request, int type);
}
