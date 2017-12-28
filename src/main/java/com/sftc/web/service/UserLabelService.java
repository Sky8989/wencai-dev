package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.UpdateUserContactLabelVO;
import com.sftc.web.model.vo.swaggerRequest.UserLabelVO;

public interface UserLabelService {
    /**
     * 根据用户id获取用户所有标签
     * @param body
     */
    ApiResponse getUserContactLabels(UserLabelVO body);

    /**
     * 根据标签id修改个人标签
     * @param body
     */
    ApiResponse updateUserContactLabels(UpdateUserContactLabelVO body);
}
