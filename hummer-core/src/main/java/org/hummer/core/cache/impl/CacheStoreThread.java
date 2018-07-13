package org.hummer.core.cache.impl;

import org.hummer.core.cache.annotation.CacheKey;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.ThreadPoolUtil;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@SuppressWarnings("all")
//@Resource
public class CacheStoreThread {
    private final static Logger log = Log4jUtils.getLogger(CacheStoreThread.class);
    private static final RedisService redisService = (RedisService) HummerContainer.getInstance().getBeanFromSpring("redisService");
    private static final boolean epirationEnabled = CacheManager.isExpirationEnabled();
    private static final int epirationPeriod = CacheManager.getExpirationPeriod();

    public static void storeResultToRedis(Object returnValue, Object redisKey, CacheKey cacheKey) {
        ThreadPoolUtil.COMMON_POOL.execute(new Runnable() {
            @Override
            public void run() {
                if (redisKey != null && cacheKey != null) {
                    log.debug("entering a new thread:{} on {}", Thread.currentThread().getName(), Thread.currentThread().toString());
                    boolean evictOnAll = cacheKey.evictOnAll();
                    //将结果集放入Redis缓存, 如果返回值为空，也继续存入redis，避免访问数据库
                    if (epirationEnabled) {
                        redisService.setWithExp(redisKey, epirationPeriod, returnValue);
                    } else {
                        redisService.set(redisKey, returnValue);
                    }

                    //如果不通过Model.Id祛除Cache，并且返回值不为空
                    if (!evictOnAll && returnValue != null) {
                        //store the rediskey and ids mapping
                        if (returnValue instanceof IModel) {
                            Long id = ((IModel) returnValue).getId();
                            CacheManager.registerRedisKeyForId(returnValue.getClass(), String.valueOf(id), redisKey);
                        } else if (returnValue instanceof Collection) {
                            // 使用jdk1.8中的新Collector实现从Collection中获取对象.方法的值返回一个集合
                            Set<Long> ids = ((Collection<IModel>) returnValue).stream().map(IModel::getId).collect(Collectors.toCollection(TreeSet::new));
                            Class modelClass = ((Collection<IModel>) returnValue).iterator().next().getClass();
                            CacheManager.registerRedisKeyForIds(modelClass, ids, redisKey);
                        } else if (returnValue instanceof Map) {
                            Set<String> ids = ((Map) returnValue).keySet();
                            Class modelClass = ((Map) returnValue).values().iterator().next().getClass();
                            CacheManager.registerRedisKeyForIdsOfStr(modelClass, ids, redisKey);
                        }
                    }
                }
            }
        });
    }
}
