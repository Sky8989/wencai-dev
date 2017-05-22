package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.UserContactLabel;

public interface UserContactLabelService {

    /**
     * 给好友添加标签
     * @param userContactLabel
     * @return
     */
    APIResponse addLabelForFriend(UserContactLabel userContactLabel);

    /**
     * 删除好友的标签
     * @param request
     * @return
     */
    APIResponse deleteLabelForFriend(APIRequest request);

    /**
     * 获取某个好友的标签
     * @param request
     * @return
     */
    APIResponse getFriendLabelList(APIRequest request);
}
