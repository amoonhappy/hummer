package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.cache.annotation.CacheEvict;
import org.hummer.core.cache.annotation.CacheEvicts;
import org.hummer.core.cache.impl.CacheEvictorThread;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.lang.reflect.Method;

/**
 * Cache Evict AOP, work with {@link CacheEvict} and {@link CacheEvicts} to determine which
 * Redis cache will be evicted from redis cache
 * {@link org.hummer.core.cache.annotation.CacheModelEvict}
 * {@link org.hummer.core.cache.annotation.CacheModelEvicts}
 */
@SuppressWarnings("all")
public class CacheEvictInterceptor extends Perl5DynamicMethodInterceptor {
    private static final Logger log = Log4jUtils.getLogger(CacheEvictInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object returnValue;
        Object targetObject = methodInvocation.getThis();
        Object[] args = methodInvocation.getArguments();
        Method method = methodInvocation.getMethod();

        //得到结果
        returnValue = methodInvocation.proceed();
        //多线程祛除Cache方法
        CacheEvictorThread cacheEvictorThread = new CacheEvictorThread();
        cacheEvictorThread.evictCaches(targetObject, args, method);
        return returnValue;
    }
}
