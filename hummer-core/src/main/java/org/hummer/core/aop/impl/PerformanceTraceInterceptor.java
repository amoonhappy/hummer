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
import org.apache.log4j.Logger;

/**
 * @author jeff.zhou
 */
public class PerformanceTraceInterceptor extends Perl5DynamicMethodInterceptor {
    private static final Logger log = Logger.getLogger(PerformanceTraceInterceptor.class);

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Class targetClass = ((CglibMethodInvocation) methodInvocation).getTargetClass();
        //TODO we can use multiple thread to write the perf log to the perf analysis log file
        Object returnValue;
        String methodName = methodInvocation.getMethod().getName();
        StringBuilder info = new StringBuilder();
        info.append("**PerLOG** [");
        info.append(targetClass.getSimpleName());
        info.append(".");
        info.append(methodName);
        info.append("] spend time: ");

        long startTime = System.currentTimeMillis();
        returnValue = methodInvocation.proceed();

        long endTime = System.currentTimeMillis();
        long spendTime = endTime - startTime;
        info.append(spendTime);
        info.append(" ms.");
        log.info(info.toString());

        return returnValue;
    }
}
