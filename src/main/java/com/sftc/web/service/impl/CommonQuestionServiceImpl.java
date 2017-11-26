package com.sftc.web.service.impl;

import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.CommonQuestionDao;
import com.sftc.web.dao.mybatis.CommonQuestionMapper;
import com.sftc.web.dao.redis.CommonQuestionRedisDao;
import com.sftc.web.model.entity.CommonQuestion;
import com.sftc.web.service.CommonQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("commonQuestionService")
public class CommonQuestionServiceImpl implements CommonQuestionService {

    @Resource
    private CommonQuestionMapper commonQuestionMapper;

    @Resource
    private CommonQuestionRedisDao commonQuestionRedisDao;

    /**
     * 获取所有常见问题
     *
     * @return
     */
    public APIResponse getCommonQuestion() {
        // 尝试从redis缓存中获取数据
        List<CommonQuestion> commonQuestionList = commonQuestionRedisDao.getCommonQuestionsFromCache();
        if (commonQuestionList == null) {
            commonQuestionList = commonQuestionMapper.getCommonQuestion();
            commonQuestionRedisDao.setCommonQuestionsToCache(commonQuestionList);
        }

        return APIUtil.getResponse(APIStatus.SUCCESS, commonQuestionList);
    }
}
