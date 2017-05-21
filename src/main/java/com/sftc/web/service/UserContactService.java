package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service
 * @Description: 联系人操作接口
 * @date 17/4/1
 * @Time 下午9:31
 */
public interface UserContactService {

    /**
     * 查询该联系人的所有好友
     * @param request
     * @return
     */
    APIResponse findUserFriend(APIRequest request);
}
