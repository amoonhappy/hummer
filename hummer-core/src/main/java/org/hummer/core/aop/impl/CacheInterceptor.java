package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.aop.intf.KeyGenerator;
import org.hummer.core.aop.intf.SimpleKeyGenerator;
import org.hummer.core.cache.annotation.CacheKey;
import org.hummer.core.cache.impl.CacheEvaluationContext;
import org.hummer.core.cache.impl.CacheStoreThread;
import org.hummer.core.cache.impl.RedisDaoImpl;
import org.hummer.core.cache.intf.ICacheable;
import org.hummer.core.cache.intf.RedisDo;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.core.PrioritizedParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * Cache AOP, work with {@link ICacheable} interface config file hummer-app-cfg-aop.xml to determine which
 * method of the a cacheable class should be cached
 * {@link CacheKey}
 */
@SuppressWarnings("all")
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
        //if false use Simple Key Generate the Cache Key
        //if true, use @CacheKey to Generate the Cache Key

        Object[] args = methodInvocation.getArguments();

        if (cacheable) {
            Object targetObject = methodInvocation.getThis();

            String cacheName = null;
            String annotationGeneratedKey = null;
            //get cache annotation
            CacheKey cacheKey = method.getAnnotation(CacheKey.class);
            //优先查询Redis
            RedisDo redisDao = new RedisDaoImpl();
            Object redisKey = genRedisKey(targetObject, method, args);
            returnValue = redisDao.RedisGet(redisKey);

            //如果Redis中没有，执行方法
            if (returnValue == null) {
                log.debug("no redis cache found for [{}].[{}]!", targetClassName, methodName);
                returnValue = methodInvocation.proceed();
                log.debug("store data to redis for [{}].[{}]", targetClassName, methodName);
            } else {
                log.debug("get data from redis for [{}].[{}]", targetClassName, methodName);
            }
            //将结果存入Redis//异步执行
            CacheStoreThread.storeResultToRedis(returnValue, redisKey, cacheKey);
        } else {
            log.info("[{}].[{}] is not defined as Cacheable, execute method to get result directly", targetClassName, methodName);
            returnValue = methodInvocation.proceed();
        }
        return returnValue;
    }

    private Object genRedisKey(Object targetObject, Method method, Object[] args) {
        CacheKey cacheKey = method.getAnnotation(CacheKey.class);
        String targetClassName = targetObject.getClass().getSimpleName();
        String methodName = method.getName();
        String cacheName = cacheKey.cacheName();
        String cacheKeyDef = cacheKey.key();
        String annotationGeneratedKey = null;

        boolean useKey = (!StringUtil.isEmpty(cacheKeyDef)) && (!StringUtil.isEmpty(cacheName));
        boolean customizedCacheKey = false;

        if (useKey) {
            try {
                ExpressionParser parser = new SpelExpressionParser();
                PrioritizedParameterNameDiscoverer parameterNameDiscoverer = new PrioritizedParameterNameDiscoverer();
                CacheEvaluationContext cacheEvaluationContext = new CacheEvaluationContext(targetObject, method, args, parameterNameDiscoverer);
                Expression expression = parser.parseExpression(cacheKeyDef);
                annotationGeneratedKey = expression.getValue(cacheEvaluationContext, String.class);
                log.debug("Defined Key in EL:{}", cacheKeyDef);
                log.debug("Generated Key:{}", annotationGeneratedKey);
                if (!StringUtil.isEmpty(annotationGeneratedKey)) {
                    customizedCacheKey = true;
                }
            } catch (Exception e) {
                log.error("Wrong CacheKey Definition Found:[{}] for [{}].[{}]", cacheKeyDef, targetClassName, methodName, e);
                customizedCacheKey = false;
            }
        } else {
            log.error("Wrong CacheKey Definition Found:[{}] for [{}].[{}]", cacheKeyDef, targetClassName, methodName);
            customizedCacheKey = false;
        }

        Object redisKey;
        //use SpEL generated key
        if (customizedCacheKey) {
            redisKey = cacheName + ":" + annotationGeneratedKey;
        } else {//User default key generator
            KeyGenerator keyGenerator = new SimpleKeyGenerator();
            redisKey = keyGenerator.generate(null, method, args);
        }
        return redisKey;
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
