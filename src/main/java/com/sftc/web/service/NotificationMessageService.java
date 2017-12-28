package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.UpdateIsReadVO;

/**
 * Created by huxingyue on 2017/6/27.
 * 通知信息 获取 / 状态更新
 */

public interface NotificationMessageService {
    /**
     * 获取 通知信息
     */
    ApiResponse getMessage();

    /**
     * 更新 通知信息 用户点击通知信息后，更改is_read状态
     * @param body
     */
    ApiResponse updateMessage(UpdateIsReadVO body);
}
