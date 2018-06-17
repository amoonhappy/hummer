package org.hummer.core.cache.impl;

import org.hummer.core.cache.annotation.CacheEvict;
import org.hummer.core.cache.annotation.CacheEvicts;
import org.hummer.core.cache.annotation.CacheModelEvict;
import org.hummer.core.cache.annotation.CacheModelEvicts;
import org.hummer.core.cache.intf.RedisDo;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.core.PrioritizedParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.Set;

@SuppressWarnings("all")
public class CacheEvictorThread {
    private final static Logger log = Log4jUtils.getLogger(CacheEvictorThread.class);

    public static void evictCaches(Object targetObject, Object[] args, Method method) {

        new Thread("CacheEvictorThread") {
            public void run() {
                log.debug("entering a new thread:{} on {}", Thread.currentThread().getName(), Thread.currentThread().toString());
                RedisDo redisDao = new RedisDaoImpl();
                //获取方法上定义的所有声明，包括CacheEvict和CacheModelEvict两种
                //先通过CacheEvict祛除Cache
                CacheEvicts cacheEvicts = method.getAnnotation(CacheEvicts.class);
                if (cacheEvicts != null) {
                    CacheEvict[] cacheEvicts1 = cacheEvicts.value();
                    for (CacheEvict cacheEvict : cacheEvicts1) {
                        evictRedisCacheByAnnoKeys(targetObject, args, method, redisDao, cacheEvict);
                    }
                } else {
                    CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
                    if (cacheEvict != null) {
                        evictRedisCacheByAnnoKeys(targetObject, args, method, redisDao, cacheEvict);
                    }
                }
                //再通过CacheModelEvict祛除Cache
                CacheModelEvicts cacheModelEvicts = method.getAnnotation(CacheModelEvicts.class);
                if (cacheModelEvicts != null) {
                    CacheModelEvict[] cacheModelEvicts1 = cacheModelEvicts.value();
                    for (CacheModelEvict cacheModelEvict : cacheModelEvicts1) {
                        evictRedisCacheByModelIds(targetObject, args, method, redisDao, cacheModelEvict);
                    }
                } else {
                    CacheModelEvict cacheModelEvict = method.getAnnotation(CacheModelEvict.class);
                    if (cacheModelEvict != null) {
                        evictRedisCacheByModelIds(targetObject, args, method, redisDao, cacheModelEvict);
                    }
                }
            }
        }.start();
    }

    private static void evictRedisCacheByAnnoKeys(Object targetObject, Object[] args, Method method, RedisDo
            redisDao, CacheEvict cacheEvict) {
        String uniKeyDef = cacheEvict.key();
        String uniCacheName = cacheEvict.cacheName();
        String uniAnnoGenRedisKey;
        boolean useKey = (!StringUtil.isEmpty(uniKeyDef)) && (!StringUtil.isEmpty(uniCacheName));
        if (useKey) {
            ExpressionParser parser = new SpelExpressionParser();
            PrioritizedParameterNameDiscoverer parameterNameDiscoverer = new PrioritizedParameterNameDiscoverer();
            CacheEvaluationContext cacheEvaluationContext = new CacheEvaluationContext(targetObject, method, args, parameterNameDiscoverer);
            Expression expression = parser.parseExpression(uniKeyDef);
            String redisKey;
            uniAnnoGenRedisKey = expression.getValue(cacheEvaluationContext, String.class);
            redisKey = uniCacheName + ":" + uniAnnoGenRedisKey;
            log.debug("Deleting from Redis for Key: {}", redisKey);
            redisDao.RedisDel(redisKey);
        }
    }

    private static void evictRedisCacheByModelIds(Object targetObject, Object[] args, Method method, RedisDo
            redisDao, CacheModelEvict cacheEvict) {
        String evictCacheKeyDef = cacheEvict.key();
        Class evictModelClass = cacheEvict.modelClass();
        String evictAnnoGenRedisKey;
        ExpressionParser evictParser = new SpelExpressionParser();
        PrioritizedParameterNameDiscoverer evictParameterNameDiscoverer = new PrioritizedParameterNameDiscoverer();
        CacheEvaluationContext evictCacheEvaluationContext = new CacheEvaluationContext(targetObject, method, args, evictParameterNameDiscoverer);
        Expression evictExpression = evictParser.parseExpression(evictCacheKeyDef);
        evictAnnoGenRedisKey = evictExpression.getValue(evictCacheEvaluationContext, String.class);
        Set<Object> linkedRedisKeys = (Set<Object>) CacheManager.getRedisKeysById(evictModelClass, evictAnnoGenRedisKey);
        log.debug("Deleting from Redis for Key: {}", linkedRedisKeys);
        redisDao.RedisDel(linkedRedisKeys);
        log.debug("Deleting from CacheManager for Key: {}", linkedRedisKeys);
        CacheManager.unRegisterRedisKeyForId(evictModelClass, evictAnnoGenRedisKey, linkedRedisKeys);
    }
}
