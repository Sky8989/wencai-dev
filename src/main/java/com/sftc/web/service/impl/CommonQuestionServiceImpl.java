package com.sftc.web.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sftc.web.dao.mybatis.CommonQuestionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.CommonQuestionDao;
import com.sftc.web.dao.redis.CommonQuestionRedisDao;
import com.sftc.web.model.entity.CommonQuestion;
import com.sftc.web.model.vo.swaggerRequest.DeleteCommonQuestionVO;
import com.sftc.web.service.CommonQuestionService;

import net.sf.json.JSONObject;
@Transactional
@Service("commonQuestionService")
public class CommonQuestionServiceImpl implements CommonQuestionService {

    @Resource
    private CommonQuestionMapper commonQuestionMapper;

    @Resource
    private CommonQuestionRedisDao commonQuestionRedisDao;

    // 获取常见问题
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
