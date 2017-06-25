package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.reqeustParam.UserContactParam;


public interface UserContactService {

//    /**
//     * 根据id获取好友信息
//     */
//    APIResponse getFriendDetail(UserContactParam userContactParam);
//
//    /**
//     * 获取某个用户的所有好友（带分页）
//     */
//    APIResponse getFriendList(Paging paging);

    /**
     * 好友圈来往记录
     */
    APIResponse getContactInfo(UserContactParam userContactParam);
}
