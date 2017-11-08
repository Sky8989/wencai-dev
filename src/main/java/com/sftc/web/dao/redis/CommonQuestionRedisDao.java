package com.sftc.web.dao.redis;


import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sftc.web.model.CommonQuestion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonQuestionRedisDao extends BaseRedisDao {

    private static final String COMMON_QUESTION_REDIS_KEY = "COMMON_QUESTION_REDIS_KEY";

    /**
     * 从缓存取常见问题列表
     */
    public List<CommonQuestion> getCommonQuestionsFromCache() {
        String commonQuestionsStr = getCache(COMMON_QUESTION_REDIS_KEY);
        if (StringUtil.isEmpty(commonQuestionsStr)) return null;

        return new Gson().fromJson(commonQuestionsStr, new TypeToken<List<CommonQuestion>>() {
        }.getType());
    }

    /**
     * 把常见问题列表放入缓存
     */
    public void setCommonQuestionsToCache(List<CommonQuestion> commonQuestions) {
        if (commonQuestions != null) {
            setCache(COMMON_QUESTION_REDIS_KEY, new Gson().toJson(commonQuestions));
        }
    }

    /**
     * 清除常见问题缓存
     */
    public void clearCommonQuestionsCache() {
        clearCache(COMMON_QUESTION_REDIS_KEY);
    }

}
