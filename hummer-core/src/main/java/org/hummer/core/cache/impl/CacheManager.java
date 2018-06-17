package org.hummer.core.cache.impl;

import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class CacheManager {
    private static final Logger log = Log4jUtils.getLogger(CacheManager.class);
    private static boolean expirationEnabled = false;
    private static int expirationPeriod = 60 * 60;
    private static ConcurrentHashMap<String, Set<Object>> classMethodParaCacheKeyMapping = new ConcurrentHashMap();

    public static boolean isExpirationEnabled() {
        return expirationEnabled;
    }

    public static void setExpirationEnabled(boolean input) {
        expirationEnabled = input;
    }

    public static int getExpirationPeriod() {
        return expirationPeriod;
    }

    public static void setExpirationPeriod(int input) {
        expirationPeriod = input;
    }

    public CacheManager() {
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForId(Class modelClass, String id, Object redisKey) {
        String modelIdKey = getModelIdKey(modelClass, id);
        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        if (redisKeys == null) {
            redisKeys = new HashSet<>();
        }
        log.debug("Redis Cache Key linked with {}, RedisCacheKey is {}", modelIdKey, redisKey);
        redisKeys.add(redisKey);
        classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForIds(Class modelClass, List<String> ids, Object redisKey) {
        //String[] idsArray = ids.split(",");
        for (String id : ids) {
            registerRedisKeyForId(modelClass, id, redisKey);
        }
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForIds(Class modelClass, String ids, Object redisKey) {
        String[] idsArray = ids.split(",");
        for (String id : idsArray) {
            registerRedisKeyForId(modelClass, id, redisKey);
        }
    }

    public static Collection getRedisKeysById(Class modelClass, String id) {
        String modelIdKey = getModelIdKey(modelClass, id);
        Collection redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        log.debug("Redis Cache Keys Found By {}, size is {}", modelIdKey, redisKeys);
        return redisKeys;
    }

    private static String getModelIdKey(Class modelClass, String id) {
        return modelClass.getName() + "@" + id;
    }

    @SuppressWarnings("all")
    public static void unRegisterRedisKeyForId(Class modelClass, String id, Object redisKey) {
        String modelIdKey = getModelIdKey(modelClass, id);
        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        if (redisKeys == null) {
            redisKeys = new HashSet<>();
        }
        log.debug("Redis Cache Key removed with {}, RedisCacheKey is {}", modelIdKey, redisKey);
        redisKeys.remove(redisKey);
        classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
    }

    public static void unRegisterRedisKeyForId(Class modelClass, String id, Collection<Object> removeRedisKeys) {
        String modelIdKey = getModelIdKey(modelClass, id);
        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        if (redisKeys == null) {
            redisKeys = new HashSet<>();
        }
        log.debug("Redis Cache Key removed with {}, RedisCacheKey is {}", modelIdKey, removeRedisKeys);
        redisKeys.removeAll(removeRedisKeys);
        classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
    }

    public static void registerRedisKeyForId(Class modelClass, String id, Collection<Object> addRedisKeys) {
        String modelIdKey = getModelIdKey(modelClass, id);
        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        if (redisKeys == null) {
            redisKeys = new HashSet<>();
        }
        redisKeys.addAll(addRedisKeys);
        log.debug("Redis Cache Key linked with {}, RedisCacheKeys is {}", modelIdKey, redisKeys);
        classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
    }
}
