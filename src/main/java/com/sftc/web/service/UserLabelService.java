package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

public interface UserLabelService {
    /**
     * 根据用户id获取用户所有标签
     * @param apiRequest
     */
    APIResponse getUserAllLabelByUCID(APIRequest apiRequest);

    /**
     * 根据标签id修改个人标签
     * @param apiRequest
     */
    APIResponse updateUsrLabelByLID(APIRequest apiRequest);

    /**
     * 根据用户好友关系id获取用户系统以及自定义标签
     * @param apiRequest
     */
    APIResponse getUserLabelDetailsByUCID(APIRequest apiRequest);
}
