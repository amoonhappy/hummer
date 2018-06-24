package org.hummer.core.cache.impl;

import org.hummer.core.cache.annotation.CacheKey;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class CacheStoreThread {
    private final static Logger log = Log4jUtils.getLogger(CacheStoreThread.class);
    private static final RedisService redisService = (RedisService) HummerContainer.getInstance().getBeanFromSpring("redisService");

    public void storeResultToRedis(Object returnValue, Object redisKey, CacheKey cacheKey) {
        new Thread("CacheStoreThread") {
            public void run() {
                log.debug("entering a new thread:{} on {}", Thread.currentThread().getName(), Thread.currentThread().toString());
                boolean evictOnAll = cacheKey.evictOnAll();
                //将结果集放入Redis缓存, 如果返回值为空，也继续存入redis，避免访问数据库
                if (CacheManager.isExpirationEnabled()) {
                    redisService.setWithExp(redisKey, CacheManager.getExpirationPeriod(), returnValue);
                } else {
                    redisService.set(redisKey, returnValue);
                }

                //如果不通过Model.Id祛除Cache，并且返回值不为空
                if (!evictOnAll && returnValue != null) {
                    //store the rediskey and ids mapping
                    if (returnValue instanceof IModel) {
                        String id = ((IModel) returnValue).getId();
                        CacheManager.registerRedisKeyForId(returnValue.getClass(), id, redisKey);
                    } else if (returnValue instanceof Collection) {
                        // 使用jdk1.8中的新Collector实现从Collection中获取对象.方法的值返回一个集合
                        Set<String> ids = ((Collection<IModel>) returnValue).stream().map(IModel::getId).collect(Collectors.toCollection(TreeSet::new));
                        Class modelClass = ((Collection<IModel>) returnValue).iterator().next().getClass();
                        CacheManager.registerRedisKeyForIds(modelClass, ids, redisKey);
                    } else if (returnValue instanceof Map) {
                        Set<String> ids = ((Map) returnValue).keySet();
                        Class modelClass = ((Map) returnValue).values().iterator().next().getClass();
                        CacheManager.registerRedisKeyForIds(modelClass, ids, redisKey);
                    }
                }
            }
        }.start();
    }
}
