package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import org.springframework.stereotype.Service;

/**
 * Created by huxingyue on 2017/6/27.
 * 通知信息 获取 / 状态更新
 */

public interface NotificationMessageService {
    //获取 通知信息
    APIResponse getMessage(APIRequest apiRequest);
    //更新 通知信息 用户点击通知信息后，更改is_read状态
    APIResponse updateMessage(APIRequest request);
}
