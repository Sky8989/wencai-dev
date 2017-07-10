package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.CommonQuestionMapper;
import com.sftc.web.model.CommonQuestion;
import com.sftc.web.service.CommonQuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("commonQuestionService")
public class CommonQuestionServiceImpl implements CommonQuestionService {

    @Resource
    private CommonQuestionMapper commonQuestionMapper;

    public APIResponse getCommonQuestion() {
        APIStatus status = APIStatus.SELECT_FAIL;
        List<CommonQuestion> commonQuestionList = commonQuestionMapper.getCommonQuestion();
        if (commonQuestionList != null) status = APIStatus.SUCCESS;
        return APIUtil.getResponse(status, commonQuestionList);
    }
}
