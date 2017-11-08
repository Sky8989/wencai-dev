package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.reqeustParam.UserContactParam;


public interface UserContactService {

    /**
     * 获取好友信息
     */
    APIResponse getFriendDetail(APIRequest request);

    /**
     * 获取用户的所有好友
     */
    APIResponse getFriendList(APIRequest request);

    /**
     * 好友圈来往记录
     */
    APIResponse getContactInfo(APIRequest request);

    /**
     * 星标/取消星标好友
     */
    APIResponse starFriend(APIRequest request);


    /**
     * 修改 好友图片与备注
     */
    APIResponse updateNotesAndPicture(APIRequest request);


    /**
     * CMS 获取好友列表 分页+条件
     */
    APIResponse selectUserContactListByPage(APIRequest request);
}
