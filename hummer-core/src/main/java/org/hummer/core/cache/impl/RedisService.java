package org.hummer.core.cache.impl;

import org.hummer.core.cache.intf.MemoryCacheService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.concurrent.TimeUnit;


/**
 * this bean is registered in Spring
 */
public class RedisService implements MemoryCacheService {

    private RedisTemplate redisTemplate;

    /**
     * 判断键是否存在
     *
     * @param key
     * @return
     */
    public boolean isExist(Object key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * set
     *
     * @param key
     * @param value
     */
    public void set(Object key, Object value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    public Object get(Object key) {
        Object ret;
        ret = redisTemplate.opsForValue().get(key);
        return ret;
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     *
     * @param key
     * @param value
     * @return
     */
    public Object getSet(Object key, Object value) {
        Object ret = redisTemplate.opsForValue().getAndSet(key, value);
        return ret;
    }

    /**
     * 设置一个有过期时间的key-value对
     *
     * @param key
     * @param seconds
     * @param value
     */
    public void setWithExp(Object key, int seconds, Object value) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 单独设置键的过期时间
     *
     * @param key
     * @param seconds
     */
    public void expire(Object key, int seconds) {
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 移除键的过期时间，键保持持久
     *
     * @param key
     */
    public void removeExp(Object key) {
        redisTemplate.persist(key);
    }

    /**
     * 查看键的剩余过期时间
     *
     * @param key
     * @return
     */
    public Long getExp(Object key) {
        Long ret;
        ret = redisTemplate.getExpire(key);
        return ret;
    }

    /**
     * 删除一个或者多个键
     *
     * @param keys
     */
    public void delete(Collection keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 删除一个或者多个键
     *
     * @param keys
     */
    public void delete(Object... keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 重命名key
     *
     * @param key
     * @param newkey
     */
    public void rename(Object key, Object newkey) {
        redisTemplate.rename(key, newkey);
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}