package com.sftc.web.dao.redis;

import org.springframework.stereotype.Component;

@Component
public class UserUnpackingRedis  extends BaseRedisDao {

    public String getUserUnpacking(String key) {
        return getCache(key);
    }

    public void setUserUnpacking(String key,String value) {
        setCache(key, value);
    }

    public void removeUserUnpacking(String key) {
        clearCache(key);
    }
}
