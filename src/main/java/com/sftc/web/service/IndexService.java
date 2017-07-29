package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

public interface IndexService {

    /**
     * 设置环境
     */
    APIResponse setupEnvironment(APIRequest request);

    /**
     * 设置公共token
     */
    APIResponse setupCommonToken(APIRequest request);


}
