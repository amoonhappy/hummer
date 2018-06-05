package org.hummer.core.aop.impl;

import com.alibaba.druid.util.StringUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.cache.annotation.CacheEvict;
import org.hummer.core.cache.annotation.CacheEvicts;
import org.hummer.core.cache.impl.CacheManager;
import org.hummer.core.cache.impl.RedisDaoImpl;
import org.hummer.core.cache.intf.RedisDo;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CacheEvictInterceptor extends Perl5DynamicMethodInterceptor {
    private static final Logger log = Log4jUtils.getLogger(CacheEvictInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object returnValue;
        Class targetClass = methodInvocation.getThis().getClass();
        String targetClassName = targetClass.getName();
        Method method = methodInvocation.getMethod();
        String methodName = method.getName();
        //check whether need to evict Redis Cache
        Annotation[] annotations = targetClass.getAnnotations();
        if (annotations != null && annotations.length > 0) {
            RedisDo redisDao = new RedisDaoImpl();

            CacheEvicts cacheEvicts = (CacheEvicts) annotations[0];
            CacheEvict[] cacheEvicts1 = cacheEvicts.value();
            if (cacheEvicts1 != null && cacheEvicts1.length > 0) {
                for (CacheEvict cacheEvict : cacheEvicts1) {
                    String evictForMethod = cacheEvict.evictForMethod();
                    String evictOnMethod = cacheEvict.evictOnMethod();
                    Class evictOnClass = cacheEvict.evictOnClass();
                    if (StringUtils.equals(methodName, evictForMethod)) {
                        log.debug("matched evictForMethod [{}].[{}] on [{}].[{}]", targetClassName, methodName, evictOnClass.getSimpleName(), evictOnMethod);
                        Object redisKey = CacheManager.getCacheKey(evictOnClass, evictOnMethod);
                        if (redisKey != null) {
                            log.debug("delete Redis key for [{}].[{}] on [{}].[{}]", targetClassName, methodName, evictOnClass.getSimpleName(), evictOnMethod);
                            redisDao.RedisDel(redisKey);
                            CacheManager.deleteCacheKey(evictOnClass, evictOnMethod);
                        } else {
                            log.error("Redis key not found in CacheManager! Pls check CacheEvict Annotation Value in [{}] Class, CacheEvict:evictForMethod=[{}],evictOnClass=[{}],evictOnMethod=[{}]", targetClassName, methodName, evictOnClass.getSimpleName(), evictOnMethod);
                        }
                        // Only 1 method to be matched
                        break;
                    }
                }
            }
        } else {
            log.debug("No Redis Cache to be evicted for [{}].[{}], Pls add CacheEvict Annotation", targetClassName, methodName);
        }
        returnValue = methodInvocation.proceed();

        return returnValue;
    }
}
