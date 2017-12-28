package com.sftc.web.dao.redis;

import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sftc.web.model.entity.Compensate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompensateRedisDao extends BaseRedisDao {

    private static final String COMPENSATE_REDIS_KEY = "COMPENSATE_REDIS_KEY";

    /**
     * 从缓存取赔偿规则说明
     */
    public List<Compensate> getCompensateFromCache() {
        String compensateStr = getCache(COMPENSATE_REDIS_KEY);
        if (StringUtil.isEmpty(compensateStr)) return null;

        return new Gson().fromJson(compensateStr, new TypeToken<List<Compensate>>() {
        }.getType());
    }

    /**
     * 把赔偿规则放入缓存
     */
    public void setCompensateToCache(List<Compensate> compensate) {
        if (compensate != null) {
            setCache(COMPENSATE_REDIS_KEY, new Gson().toJson(compensate));
        }
    }

    /**
     * 清除赔偿规则缓存
     */
    public void clearCompensateCache() {
        clearCache(COMPENSATE_REDIS_KEY);
    }

}
