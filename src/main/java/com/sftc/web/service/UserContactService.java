package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.FriendListVO;
import com.sftc.web.model.vo.swaggerRequest.FriendStarVO;
import com.sftc.web.model.vo.swaggerRequest.UserContactParamVO;


public interface UserContactService {

    /**
     * 获取好友信息
     */
    ApiResponse getFriendDetail(String friendUUId);

    /**
     * 获取用户的所有好友
     */
    ApiResponse getFriendList(FriendListVO body);

    /**
     * 好友圈来往记录
     */
    ApiResponse getContactInfo(UserContactParamVO body);

    /**
     * 星标/取消星标好友
     */
    ApiResponse starFriend(FriendStarVO body);


}
