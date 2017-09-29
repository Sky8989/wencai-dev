package com.sftc.web.dao.redis;

import com.sftc.web.modules.nosql.redis.JedisTemplate;
import com.sftc.web.modules.nosql.redis.pool.JedisPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

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
    private JedisTemplate jedisTemplate;

    public String getCache(String key) {
        boolean broken = false;
        Jedis jedis;
        JedisPool jedisPool = jedisTemplate.getJedisPool();
        String value = null;
        try {
            jedis = jedisPool.getResource();
            if (key != null) {
                value = jedis.get(key);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            jedisTemplate.closeResource(broken);
        }
        return value;
    }

    public String setCache(String key, String value) {
        boolean broken = false;
        Jedis jedis;
        JedisPool jedisPool = jedisTemplate.getJedisPool();
        try {
            jedis = jedisPool.getResource();
            if (key != null) {
                return jedis.setex(key, 60 * 5, value);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            jedisTemplate.closeResource(broken);
        }
        return null;
    }

}