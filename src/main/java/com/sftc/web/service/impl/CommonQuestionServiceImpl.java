package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.CommonQuestionMapper;
import com.sftc.web.model.CommonQuestion;
import com.sftc.web.service.CommonQuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service.impl
 * @Description: 常见问题操作接口实现
 * @date 2017/4/25
 * @Time 上午10:59
 */
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
