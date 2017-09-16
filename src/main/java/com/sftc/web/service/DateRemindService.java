package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.DateRemind;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description: 日期提醒操作接口
 * @date 2017/4/25
 * @Time 上午10:57
 */
public interface DateRemindService {

    /**
     * 添加 好友日期提醒
     *
     * @param apiRequest
     * @return
     */
    APIResponse addFriendDateRemind(APIRequest apiRequest);

    /**
     * 删除 好友日期提醒
     *
     * @param request
     * @return
     */
    APIResponse deleteFriendDateRemind(APIRequest request);

    /**
     * 获取 好友日期提醒 列表
     *
     * @param request
     * @return
     */
    APIResponse selectFriendDateRemind(APIRequest request);
}
