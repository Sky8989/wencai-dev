package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.CommonQuestion;

public interface CommonQuestionService {

    /**
     * 获取所有常见问题
     */
    APIResponse getCommonQuestion();

}
