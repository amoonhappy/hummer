package org.hummer.core.cache.impl;

import org.apache.commons.collections.CollectionUtils;
import org.hummer.core.cache.intf.MemoryCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * this bean is registered in Spring
 */
public class RedisService implements MemoryCacheService {

    private RedisTemplate redisTemplate;
    private StringRedisTemplate stringRedisTemplate;
    RedisSerializer<String> stringSerializer = new StringRedisSerializer();

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public RedisService() {
    }

    /**
     * 判断键是否存在
     *
     * @param key
     * @return
     */
    public boolean isExist(Object key) {
        redisTemplate.setKeySerializer(stringSerializer);
        return redisTemplate.hasKey(key);
    }

    public void deleteByPrex(String prex) {
        redisTemplate.setKeySerializer(stringSerializer);

//        Set<String> keys = stringRedisTemplate.keys(prex);
        Set<String> keys = redisTemplate.keys(prex);

        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }


    }

    /**
     * set
     *
     * @param key
     * @param value
     */
    public void set(Object key, Object value) {
        redisTemplate.setKeySerializer(stringSerializer);

        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    public Object get(Object key) {
        Object ret;
        redisTemplate.setKeySerializer(stringSerializer);

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
        redisTemplate.setKeySerializer(stringSerializer);

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
        redisTemplate.setKeySerializer(stringSerializer);

        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 单独设置键的过期时间
     *
     * @param key
     * @param seconds
     */
    public void expire(Object key, int seconds) {

        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 移除键的过期时间，键保持持久
     *
     * @param key
     */
    public void removeExp(Object key) {

        redisTemplate.setKeySerializer(stringSerializer);
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

        redisTemplate.setKeySerializer(stringSerializer);

        ret = redisTemplate.getExpire(key);
        return ret;
    }

    /**
     * 删除一个或者多个键
     *
     * @param keys
     */
    public void delete(Collection keys) {
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.delete(keys);
    }

    /**
     * 删除一个或者多个键
     *
     * @param keys
     */
    public void delete(Object keys) {
        String key = (String) keys;
        redisTemplate.delete(key);
    }

    /**
     * 重命名key
     *
     * @param key
     * @param newkey
     */
    public void rename(Object key, Object newkey) {
        redisTemplate.setKeySerializer(stringSerializer);

        redisTemplate.rename(key, newkey);
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}