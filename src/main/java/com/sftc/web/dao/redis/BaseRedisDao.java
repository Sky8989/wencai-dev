package com.sftc.web.dao.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * redis缓存基类，封装设置缓存和取缓存的方法
 *
 * @author bingo
 */
@Repository
public class BaseRedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    protected String getCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    protected void setCache(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    protected void clearCache(String key) {
        redisTemplate.delete(key);
    }

}