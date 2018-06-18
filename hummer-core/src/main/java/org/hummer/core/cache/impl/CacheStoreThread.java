package org.hummer.core.cache.impl;

import org.hummer.core.cache.annotation.CacheKey;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@SuppressWarnings("all")
public class CacheStoreThread {
    private final static Logger log = Log4jUtils.getLogger(CacheStoreThread.class);
    RedisDaoImpl redisService;

    public void storeResultToRedis(Object returnValue, Object redisKey, CacheKey cacheKey) {
        new Thread("CacheStoreThread") {
            public void run() {
                redisService = (RedisDaoImpl) HummerContainer.getInstance().getBeanFromSpring("redisService");
                log.debug("entering a new thread:{} on {}", Thread.currentThread().getName(), Thread.currentThread().toString());
                boolean evictOnAll = cacheKey.evictOnAll();
                //将结果集放入Redis缓存, 如果返回值为空，也继续存入redis，避免访问数据库
                if (CacheManager.isExpirationEnabled()) {
                    redisService.RedisSetex(redisKey, CacheManager.getExpirationPeriod(), returnValue);
                } else {
                    redisService.RedisSet(redisKey, returnValue);
                }

                //如果不通过Model.Id祛除Cache，并且返回值不为空
                if (!evictOnAll && returnValue != null) {
                    //store the rediskey and ids mapping
                    if (returnValue instanceof IModel) {
                        //redisDao.RedisSet(redisKey + ".ids", ((IModel) returnValue).getId());
                        String id = ((IModel) returnValue).getId();
                        CacheManager.registerRedisKeyForId(returnValue.getClass(), id, redisKey);
                    } else if (returnValue instanceof Collection) {
                        String idstr = (String) ((Collection<IModel>) returnValue).stream().map(IModel::getId).collect(joining(","));
                        Class modelClass = ((Collection<IModel>) returnValue).iterator().next().getClass();
                        CacheManager.registerRedisKeyForIds(modelClass, idstr, redisKey);
                    } else if (returnValue instanceof Map) {
                        Collection<String> ids = ((Map) returnValue).keySet();
                        String idstr = ids.stream().collect(joining(","));
                        Class modelClass = ((Map) returnValue).values().iterator().next().getClass();
                        CacheManager.registerRedisKeyForIds(modelClass, idstr, redisKey);
                    }
                }
            }
        }.start();
    }
}
