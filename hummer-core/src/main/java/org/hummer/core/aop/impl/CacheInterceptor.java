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
import java.lang.reflect.Parameter;

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
        boolean cacheable = isCacheable(targetClass);
        //如果类实现了ICacheable接口
        if (cacheable) {
            RedisDo redisDao = new RedisDaoImpl();
            KeyGenerator keyGenerator = new SimpleKeyGenerator();
            int paramCount = method.getParameterCount();
            Parameter[] parameters = method.getParameters();
            Object[] params = new Object[paramCount];
            for (int i = 0; i < paramCount; i++) {
                params[i] = parameters[i].hashCode();
            }
            Object key = keyGenerator.generate(null, method, params);
            log.info("[{}].[{}] is cacheable\ntry to retrieve from redis cache!", targetClassName, methodName);

            //优先查询Redis
            returnValue = redisDao.RedisGet(key);
            if (returnValue == null) {
                log.debug("no redis cache found for [{}].[{}]!", targetClassName, methodName);
                //如果Redis中没有，执行方法
                returnValue = methodInvocation.proceed();
                //将结果集放入Redis缓存
                redisDao.RedisSet(key, returnValue);
                //将key=类名+方法名 和 value = keyGenerator生成的RedisKye保存起来
                CacheManager.registerCacheKey(targetClass, method, key);
                log.debug("store redis key to CacheManager for [{}].[{}]", targetClassName, methodName);
                log.debug("store data to redis for [{}].[{}]", targetClassName, methodName);
            } else {
                log.debug("get data from redis for [{}].[{}]", targetClassName, methodName);
            }
        } else {
            log.info("[{}].[{}] is not cacheable\nexecute method to get result directly", targetClassName, methodName);
            returnValue = methodInvocation.proceed();
        }
        return returnValue;
    }

    private boolean isCacheable(Class targetClass) {
        boolean ret = false;
        Class[] interfaces = targetClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (int i = 0; i < interfaces.length; i++) {
                Class ints = interfaces[i];
                if (ints != null && ints.equals(ICacheable.class)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }
}
