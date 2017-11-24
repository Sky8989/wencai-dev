package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

public interface UserLabelService {
    /**
     * 根据用户id获取用户所有标签
     */
    APIResponse getUserContactLabels(APIRequest apiRequest);

    /**
     * 根据标签id修改个人标签
     */
    APIResponse updateUserContactLabels(APIRequest apiRequest);


	APIResponse addUserContactLabels(APIRequest apiRequest);


	APIResponse deleteUserContactLabels(APIRequest apiRequest);
}
