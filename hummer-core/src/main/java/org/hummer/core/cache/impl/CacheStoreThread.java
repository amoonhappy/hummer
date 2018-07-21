package org.hummer.core.cache.impl;

import org.hummer.core.cache.annotation.CacheKey;
import org.hummer.core.container.HummerContainer;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.model.intf.IStringPKModel;
import org.hummer.core.pagination.PageUtils;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.ThreadPoolUtil;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
//@Resource
public class CacheStoreThread {
    private final static Logger log = Log4jUtils.getLogger(CacheStoreThread.class);
    private static final RedisService redisService = (RedisService) HummerContainer.getInstance().getBeanFromSpring("redisService");
    private static final boolean epirationEnabled = CacheManager.isExpirationEnabled();
    private static final int epirationPeriod = CacheManager.getExpirationPeriod();

    /**
     * 1. 将key和value保持到redis
     * 2. 解析value中保持的Model（实现IModel或者IStringPKModel接口的实体类）对应的ID
     * 3. 通过modelKey(Model类名+Id)= values（第1步中的Key），保存实体Model缓存与Redis缓存Keys之间的关联
     * 4. 在CacheModelEvict实现中，再通过生成的modelKey找到所有关联redis缓存的Key，然后在redis中删除缓存
     * 5. 在CacheEvict实现中，直接通过cacheName+“:”+key，删除指定key的缓存，如果key为通配符“*”则删除所有相关的缓存
     *
     * @param returnValue
     * @param redisKey
     * @param cacheKey
     */
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
                        if (returnValue instanceof IStringPKModel) {
                            String id = ((IStringPKModel) returnValue).getId();
                            CacheManager.registerRedisKeyForId(returnValue.getClass(), id, redisKey);
                        } else if (returnValue instanceof IModel) {
                            //store the rediskey and ids mapping
                            Long id = ((IModel) returnValue).getId();
                            CacheManager.registerRedisKeyForId(returnValue.getClass(), String.valueOf(id), redisKey);
                        } else if (returnValue instanceof Collection) {
                            // 使用jdk1.8中的新Collector实现从Collection中获取对象.方法的值返回一个集合
                            if (((Collection) returnValue).iterator().next() instanceof IModel) {
                                if (returnValue != null && ((Collection) returnValue).size() > 0) {
                                    Set<Long> ids = ((Collection<IModel>) returnValue).stream().map(IModel::getId).collect(Collectors.toCollection(TreeSet::new));
                                    Class modelClass = ((Collection<IModel>) returnValue).iterator().next().getClass();
                                    CacheManager.registerRedisKeyForIds(modelClass, ids, redisKey);
                                }
                            } else if (((Collection) returnValue).iterator().next() instanceof IStringPKModel) {
                                if (returnValue != null && ((Collection) returnValue).size() > 0) {
                                    Set<String> ids = ((Collection<IStringPKModel>) returnValue).stream().map(IStringPKModel::getId).collect(Collectors.toCollection(TreeSet::new));
                                    Class modelClass = ((Collection<IStringPKModel>) returnValue).iterator().next().getClass();
                                    CacheManager.registerRedisKeyForIdsOfStr(modelClass, ids, redisKey);
                                }
                            }
                        } else if (returnValue instanceof Map) {
                            if (returnValue != null && ((Map) returnValue).size() > 0) {
                                Set<String> ids = ((Map) returnValue).keySet();
                                Class modelClass = ((Map) returnValue).values().iterator().next().getClass();
                                CacheManager.registerRedisKeyForIdsOfStr(modelClass, ids, redisKey);
                            }
                        } else if (returnValue instanceof PageUtils) {
                            List<?> results = ((PageUtils) returnValue).getList();
                            Object temp = results.get(0);
                            if (temp != null && temp instanceof IModel) {
                                Class modelClass = temp.getClass();
                                Set<Long> ids = ((Collection<IModel>) results).stream().map(IModel::getId).collect(Collectors.toCollection(TreeSet::new));
                                CacheManager.registerRedisKeyForIds(modelClass, ids, redisKey);
                            } else if (temp != null && temp instanceof IStringPKModel) {
                                Set<String> ids = ((Collection<IStringPKModel>) results).stream().map(IStringPKModel::getId).collect(Collectors.toCollection(TreeSet::new));
                                Class modelClass = temp.getClass();
                                CacheManager.registerRedisKeyForIdsOfStr(modelClass, ids, redisKey);
                            }
                        }
                    }
                }
            }
        });
    }
}
