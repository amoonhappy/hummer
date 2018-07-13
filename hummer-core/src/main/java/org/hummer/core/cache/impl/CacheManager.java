package org.hummer.core.cache.impl;

import org.apache.commons.collections.FastHashMap;
import org.hummer.core.util.Assert;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.util.*;

@SuppressWarnings("unchecked")
public class CacheManager {
    private static final Logger log = Log4jUtils.getLogger(CacheManager.class);
    private static boolean expirationEnabled = false;
    private static int expirationPeriod = 60 * 60;
    private static Map<String, Set<Object>> classMethodParaCacheKeyMapping = new FastHashMap();
    // key = class name +
    private static Map<String, String> springELgenRedisKeyCacheKey = new FastHashMap();


    static boolean isExpirationEnabled() {
        return expirationEnabled;
    }

    public static void setExpirationEnabled(boolean input) {
        expirationEnabled = input;
    }

    static int getExpirationPeriod() {
        return expirationPeriod;
    }

    public static void setExpirationPeriod(int input) {
        expirationPeriod = input;
    }

    public CacheManager() {
    }

    public static String getGenRedisKeyFromCache(String key) {

        if (key == null) return null;
        if (springELgenRedisKeyCacheKey.containsKey(key)) {
            return springELgenRedisKeyCacheKey.get(key);
        } else {
            return null;
        }
    }

    public static void setGenRedisKey2Cache(String key, String redisKey) {
        if (key != null && redisKey != null) {
            springELgenRedisKeyCacheKey.put(key, redisKey);
        } else {
            //do nothing
        }
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForId(Class modelClass, String id, Object redisKey) {
        String modelIdKey = getModelIdKey(modelClass, id);
        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        if (redisKeys == null) {
            redisKeys = new HashSet<>();
        }
        //log.debug("Redis Cache Key linked with {}, RedisCacheKey is {}", modelIdKey, redisKey);
        redisKeys.add(redisKey);
        classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForIdsOfStr(Class modelClass, Set<String> ids, Object redisKey) {
        //String[] idsArray = ids.split(",");
        for (String id : ids) {
            String modelIdKey = getModelIdKey(modelClass, id);
            Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
            if (redisKeys == null) {
                redisKeys = new HashSet<>();
            }
            redisKeys.add(redisKey);
            classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
        }
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForIds(Class modelClass, Set<Long> ids, Object redisKey) {
        //String[] idsArray = ids.split(",");
        for (Long id : ids) {
            String modelIdKey = getModelIdKey(modelClass, id);
            Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
            if (redisKeys == null) {
                redisKeys = new HashSet<>();
            }
            redisKeys.add(redisKey);
            classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
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
        //log.debug("Redis Cache Keys Found By {}, size is {}", modelIdKey, redisKeys);
        return redisKeys;
    }

    private static String getModelIdKey(Class modelClass, String id) {
        return modelClass.getName() + "@" + id;
    }

    private static String getModelIdKey(Class modelClass, Long id) {
        return getModelIdKey(modelClass, String.valueOf(id));
    }

    @SuppressWarnings("all")
    public static void unRegisterRedisKeyForId(Class modelClass, String id, Object redisKey) {
        String modelIdKey = getModelIdKey(modelClass, id);
        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        if (redisKeys == null) {
            redisKeys = new HashSet<>();
        }
        //log.debug("Redis Cache Key removed with {}, RedisCacheKey is {}", modelIdKey, redisKey);
        redisKeys.remove(redisKey);
        classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
    }

    public static void unRegisterRedisKeyForId(Class modelClass, String id, Collection<Object> removeRedisKeys) {
        String modelIdKey = getModelIdKey(modelClass, id);
        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        if (redisKeys == null) {
            redisKeys = new HashSet<>();
        }
        //log.debug("Redis Cache Key removed with {}, RedisCacheKey is {}", modelIdKey, removeRedisKeys);
        if (removeRedisKeys != null) {
            redisKeys.removeAll(removeRedisKeys);
            classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
        }
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

    public static String getGeneratedKeyCacheKey(Object[] args, String targetClassName, String methodName, String cacheName, String cacheKeyDef) {
        Assert.notNull(targetClassName, "target Class Name should not be empty");
        Assert.notNull(methodName, "Method should not be empty");
        Assert.notNull(cacheName, "CacheName should not be empty");
        Assert.notNull(cacheKeyDef, "CacheKeyDef should not be empty");
        Assert.notNull(args, "Arguments should not be empty");
        String argstr = Arrays.deepToString(args);

        return targetClassName + methodName + cacheName + cacheKeyDef + argstr;
    }
}
