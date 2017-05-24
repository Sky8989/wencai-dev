package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Paging;
import com.sftc.web.model.reqeustParam.UserContactParam;

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
     * 添加好友
     * @param userContactParam
     * @return
     */
    APIResponse addFriend(UserContactParam userContactParam);

    /**
     * 根据id获取好友信息
     * @param id
     * @return
     */
    APIResponse getFriendDetail(UserContactParam userContactParam);

    /**
     * 获取某个用户的所有好友（带分页）
     * @param paging
     * @return
     */
    APIResponse getFriendList(Paging paging);
}
