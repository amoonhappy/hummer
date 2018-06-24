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
package org.hummer.core.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.aop.impl.Perl5DynamicMethodInterceptor;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.lang.reflect.Method;

/**
 * @author jeff.zhou
 */
public class PerformanceTraceInterceptor extends Perl5DynamicMethodInterceptor {
    private static final Logger log = Log4jUtils.getLogger(PerformanceTraceInterceptor.class);

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String name = this.createInvocationTraceName(methodInvocation);
        StopWatch stopWatch = new StopWatch(name);
        stopWatch.start(name);

        Object returnValue;
        try {
            returnValue = methodInvocation.proceed();
        } finally {
            stopWatch.stop();
            log.debug(stopWatch.shortSummary());
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

        sb.append(clazz.getSimpleName());
        sb.append('.').append(method.getName());
        return sb.toString();
    }
}
