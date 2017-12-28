package com.sftc.web.service;


import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.UserUnpackingVO;

import javax.servlet.http.HttpServletRequest;

public interface UserUnpackingService {

    ApiResponse unpacking(UserUnpackingVO body, HttpServletRequest request);
}
