/*
  <p>Open Source Architecture Project -- Hummer            </p>
  <p>Class Description                                     </p>
  <p>                                                      </p>
  <p>                                                      </p>
  <p>Change History                                        </p>
  <p>Author    Date      Description                       </p>
  <p>                                                      </p>
  <p>                                                      </p>

  @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-6
 * @version 1.0
 */
package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jeff.zhou
 */
public class PerformanceTraceInterceptor extends Perl5DynamicMethodInterceptor {
    private static final Logger log = Log4jUtils.getLogger(PerformanceTraceInterceptor.class);
    Lock lock = new ReentrantLock();

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
//        Class targetClass = ((CglibMethodInvocation) methodInvocation).getTargetClass();
//        //TODO we can use multiple thread to write the perf log to the perf analysis log file
//        Object returnValue;
//        String methodName = methodInvocation.getMethod().getName();
//        String simpleName = targetClass.getSimpleName();
//
//        long startTime = System.currentTimeMillis();
//        returnValue = methodInvocation.proceed();
//
//        long endTime = System.currentTimeMillis();
//        long spendTime = endTime - startTime;
//        log.trace("[{}.{}] spend time: {} ms.", simpleName, methodName, spendTime);
        String name = this.createInvocationTraceName(methodInvocation);
        StopWatch stopWatch = new StopWatch(name);
        stopWatch.start(name);

        Object returnValue;
        try {
            returnValue = methodInvocation.proceed();
        } finally {
            stopWatch.stop();
            log.info(stopWatch.shortSummary());
        }

        return returnValue;
    }

    protected String createInvocationTraceName(MethodInvocation invocation) {
        StringBuilder sb = new StringBuilder();
        Method method = invocation.getMethod();
        //Class<?> clazz = method.getDeclaringClass();
        //if (this.logTargetClassInvocation && clazz.isInstance(invocation.getThis())) {
        Class clazz = invocation.getThis().getClass();
        //}

        sb.append(clazz.getName());
        sb.append('.').append(method.getName());
        return sb.toString();
    }
}
