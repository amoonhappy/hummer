package org.hummer.core.cache.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.hummer.core.container.HummerContainer;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class CacheManager {
    private static final Logger log = Log4jUtils.getLogger(CacheManager.class);
    private static boolean expirationEnabled = false;
    private static int expirationPeriod = 60 * 60;
//    private static Map<String, Set<Object>> classMethodParaCacheKeyMapping = new FastHashMap();

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

    public static Map<?, ?> getClassMethodParaCacheKeyMapping() {
//        return classMethodParaCacheKeyMapping;
        return null;
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForId(Class modelClass, String id, Object redisKey) {
        RedisService redisService = (RedisService) HummerContainer.getInstance().getBeanFromSpring("redisService");
        String modelIdKey = getModelIdKey(modelClass, id);
        Set<Object> redisKeys = (Set<Object>) redisService.get(modelIdKey);
//        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        if (redisKeys == null) {
            redisKeys = new HashSet<>();
        }
        log.debug("Redis Cache Key linked with {}, RedisCacheKey is {}", modelIdKey, JSONObject.toJSONString(redisKey));
        redisKeys.add(redisKey);
        redisService.delete(modelIdKey);
        redisService.set(modelIdKey, redisKeys);
//        classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForIdsOfStr(Class modelClass, Set<String> ids, Object redisKey) {
        RedisService redisService = (RedisService) HummerContainer.getInstance().getBeanFromSpring("redisService");
        //String[] idsArray = ids.split(",");
        for (String id : ids) {
            String modelIdKey = getModelIdKey(modelClass, id);
//            Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
            Set<Object> redisKeys = (Set<Object>) redisService.get(modelIdKey);

            if (redisKeys == null) {
                redisKeys = new HashSet<>();
            }
            redisKeys.add(redisKey);
            log.debug("Redis Cache Key linked with {}, RedisCacheKeys is {}", modelIdKey, JSONArray.toJSONString(redisKeys));
//            classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
            redisService.set(modelIdKey, redisKeys);
        }
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForIds(Class modelClass, Set<Long> ids, Object redisKey) {
        RedisService redisService = (RedisService) HummerContainer.getInstance().getBeanFromSpring("redisService");
        //String[] idsArray = ids.split(",");
        for (Long id : ids) {
            String modelIdKey = getModelIdKey(modelClass, id);
//            Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
            Set<Object> redisKeys = (Set<Object>) redisService.get(modelIdKey);

            if (redisKeys == null) {
                redisKeys = new HashSet<>();
            }
            redisKeys.add(redisKey);
            log.debug("Redis Cache Key linked with {}, RedisCacheKeys is {}", modelIdKey, JSONArray.toJSONString(redisKeys));
//            classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
            redisService.set(modelIdKey, redisKeys);
        }
    }

    @SuppressWarnings("all")
    public static void registerRedisKeyForIds(Class modelClass, String ids, Object redisKey) {
        String[] idsArray = ids.split(",");
        for (String id : idsArray) {
            registerRedisKeyForId(modelClass, id, redisKey);
        }
    }

    static Collection getRedisKeysById(Class modelClass, String id) {
        RedisService redisService = (RedisService) HummerContainer.getInstance().getBeanFromSpring("redisService");
        String modelIdKey = getModelIdKey(modelClass, id);
//        Collection redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        Set<Object> redisKeys = (Set<Object>) redisService.get(modelIdKey);
        log.debug("Redis Cache Keys Found By {}, size is {}", modelIdKey, JSONArray.toJSONString(redisKeys));
        return redisKeys;
    }

    private static String getModelIdKey(Class modelClass, String id) {
        return modelClass.getName() + "@" + id;
    }

    private static String getModelIdKey(Class modelClass, Long id) {
        return getModelIdKey(modelClass, String.valueOf(id));
    }

//    @SuppressWarnings("all")
//    public static void unRegisterRedisKeyForId(Class modelClass, String id, Object redisKey) {
//        String modelIdKey = getModelIdKey(modelClass, id);
//        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
//        if (redisKeys == null) {
//            redisKeys = new HashSet<>();
//        } else if (redisKeys.size() > 0) {
//            log.debug("Redis Cache Key removed with {}, RedisCacheKey is {}", modelIdKey, JSONObject.toJSONString(redisKey));
//            redisKeys.remove(redisKey);
//        }
//        classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
//    }

    static void unRegisterRedisKeyForId(Class modelClass, String id, Collection<Object> removeRedisKeys) {
        RedisService redisService = (RedisService) HummerContainer.getInstance().getBeanFromSpring("redisService");
        String modelIdKey = getModelIdKey(modelClass, id);
//        Set<Object> redisKeys = classMethodParaCacheKeyMapping.get(modelIdKey);
        Set<Object> redisKeys = (Set<Object>) redisService.get(modelIdKey);
        if (redisKeys == null) {
            redisKeys = new HashSet<>();
        }
        if (removeRedisKeys != null && redisKeys.size() > 0) {
            log.debug("Redis Cache Key removed with {}, RedisCacheKey is {}", modelIdKey, JSONArray.toJSONString(removeRedisKeys));
            redisKeys.removeAll(removeRedisKeys);
//            classMethodParaCacheKeyMapping.put(modelIdKey, redisKeys);
            redisService.set(modelIdKey, redisKeys);
        }
    }
}
