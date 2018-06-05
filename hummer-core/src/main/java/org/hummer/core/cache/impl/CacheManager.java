package org.hummer.core.cache.impl;

import org.hummer.core.cache.intf.RedisDo;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.lang.reflect.Method;

public class CacheManager {
    private static final Logger log = Log4jUtils.getLogger(CacheManager.class);

    public CacheManager() {
    }

    public static void registerCacheKey(String key, Object value) {
        RedisDo redisDo = new RedisDaoImpl();
        redisDo.RedisSet(key, value);
    }

    public static void registerCacheKey(Class clazz, Method method, Object value) {
        String key = clazz.getSimpleName() + method.getName();
        registerCacheKey(key, value);
    }

    public static Object getCacheKey(Class clazz, String methodName) {
        Object ret = null;
        if (clazz != null && methodName != null) {
            String key = clazz.getSimpleName() + methodName;
            ret = getCacheKey(key);
        }
        return ret;
    }

    public static Object getCacheKey(String key) {
        RedisDo redisDo = new RedisDaoImpl();
        Object ret = null;
        if (key != null) {
            ret = redisDo.RedisGet(key);
        }
        return ret;
    }

    public static void deleteCacheKey(Class clazz, String methodName) {
        if (clazz != null && methodName != null) {
            String key = clazz.getSimpleName() + methodName;
            deleteCacheKey(key);
        }
    }

    public static void deleteCacheKey(String key) {
        RedisDo redisDo = new RedisDaoImpl();
        redisDo.RedisDel(key);
    }
}
