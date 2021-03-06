package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

/**
 * Author:hxy starmoon1994
 * Description: token机制的总入口
 * Date:10:28 2017/8/8
 */
public interface TokenService {

    /**
     * 为CMS提供token列表查询接口
     * @param apiRequest
     * @return
     * @throws Exception
     */
    APIResponse getTokenList(APIRequest apiRequest) throws Exception;

	APIResponse deleteToken(APIRequest apiRequest) throws Exception;
}
