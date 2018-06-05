package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.aop.intf.KeyGenerator;
import org.hummer.core.aop.intf.SimpleKeyGenerator;
import org.hummer.core.cache.impl.CacheManager;
import org.hummer.core.cache.impl.RedisDaoImpl;
import org.hummer.core.cache.intf.ICacheable;
import org.hummer.core.cache.intf.RedisDo;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.lang.reflect.Method;

/**
 * Cache Evict AOP, work with {@link ICacheable} interface config file hummer-app-cfg-aop.xml to determine which
 * method of the a cacheable class should be cached
 */
public class CacheInterceptor extends Perl5DynamicMethodInterceptor {
    private static final Logger log = Log4jUtils.getLogger(CacheInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object returnValue;
        Class targetClass = methodInvocation.getThis().getClass();
        String targetClassName = targetClass.getName();
        Method method = methodInvocation.getMethod();
        String methodName = method.getName();
        Class[] interfaces = targetClass.getInterfaces();
        boolean cacheable = false;
        if (interfaces != null && interfaces.length > 0) {
            for (int i = 0; i < interfaces.length; i++) {
                Class ints = interfaces[i];
                if (ints != null && ints.equals(ICacheable.class)) {
                    cacheable = true;
                    break;
                }
            }
        }
        //如果类实现了ICacheable接口
        if (cacheable) {
            RedisDo redisDao = new RedisDaoImpl();
            KeyGenerator keyGenerator = new SimpleKeyGenerator();
            Object key = keyGenerator.generate(null, method, method.getParameters());
            log.info("generated key is [{}]", key);

            //优先查询Redis
            returnValue = redisDao.RedisGet(key);
            if (returnValue == null) {
                //如果Redis中没有，执行方法
                returnValue = methodInvocation.proceed();
                //将结果集放入Redis缓存
                redisDao.RedisSet(key, returnValue);
                //将key=类名+方法名 和 value = keyGenerator生成的RedisKye保存起来
                CacheManager.registerCacheKey(targetClass, method, key);
                log.info("store redis key to CacheManager for [{}].[{}]", targetClassName, methodName);
                log.info("store data to redis for [{}].[{}]", targetClassName, methodName);
            } else {
                log.info("get data from redis for [{}].[{}]", targetClassName, methodName);
                log.info("data is [{}]", returnValue);
            }
        } else {
            returnValue = methodInvocation.proceed();
        }
        return returnValue;
    }
}
