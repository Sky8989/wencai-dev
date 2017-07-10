package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description: 常见问题操作接口
 * @date 2017/4/25
 * @Time 上午10:58
 */
public interface CommonQuestionService {

    /**
     * 获取所有常见问题
     * @return
     */
    APIResponse getCommonQuestion();
}
