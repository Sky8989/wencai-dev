package com.sftc.web.service;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.SwaggerRequestVO.UserUnpackingVO;

import javax.servlet.http.HttpServletRequest;

public interface UserUnpackingService {

    APIResponse unpacking(APIRequest apiRequest);
}
