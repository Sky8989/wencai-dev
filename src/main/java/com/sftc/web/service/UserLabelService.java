package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;

public interface UserLabelService {
    /**
     * 根据用户id获取用户所有标签
     */
    APIResponse getUserAllLabelByUCID(int user_id);

    /**
     * 根据标签id修改个人标签
     */
    APIResponse updateUsrLabelByLID(int label_id, String labels, int type);

    /**
     * 根据用户好友关系id获取用户系统以及自定义标签
     */
    APIResponse getUserLabelDetailsByUCID(int user_contact_id);
}
