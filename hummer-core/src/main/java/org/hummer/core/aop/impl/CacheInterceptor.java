package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.aop.intf.KeyGenerator;
import org.hummer.core.aop.intf.SimpleKeyGenerator;
import org.hummer.core.cache.impl.RedisDaoImpl;
import org.hummer.core.cache.intf.ICacheable;
import org.hummer.core.cache.intf.RedisDo;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.lang.reflect.Method;

public class CacheInterceptor extends Perl5DynamicMethodInterceptor {
    private static final Logger log = Log4jUtils.getLogger(CacheInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object returnValue;
        Class targetClass = methodInvocation.getThis().getClass();
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

        if (cacheable) {
            Method method = methodInvocation.getMethod();

            KeyGenerator keyGenerator = new SimpleKeyGenerator();
            Object key = keyGenerator.generate(null, method, method.getParameters());
            String name = method.getName();
            String targetClassName = targetClass.getName();
            log.info("generated key is [{}]", key);

            RedisDo redisDao = new RedisDaoImpl();
            //优先查询Redis
            returnValue = redisDao.RedisGet(key);
            if (returnValue == null) {
                //如果Redis中没有，执行方法
                returnValue = methodInvocation.proceed();
                //将结果集放入Redis缓存
                redisDao.RedisSet(key, returnValue);
                log.info("store to redis for [{}].[{}]", targetClassName, name);
            } else {
                log.info("get from redis for [{}].[{}]", targetClassName, name);
                log.info("return value is [{}]", returnValue);
            }
        } else {
            returnValue = methodInvocation.proceed();
        }
        return returnValue;
    }
}
