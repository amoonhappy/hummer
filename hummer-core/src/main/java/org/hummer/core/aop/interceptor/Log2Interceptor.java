package org.hummer.core.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.aop.impl.Perl5DynamicMethodInterceptor;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class Log2Interceptor extends Perl5DynamicMethodInterceptor {
    private static Logger log = Log4jUtils.getLogger(Log2Interceptor.class);

    public Object invoke(MethodInvocation arg0) throws Throwable {
        String methodName = arg0.getMethod().getName();
        log.info("Log2Interceptor:before {} invoke", methodName);
        Object ret = arg0.proceed();
        log.info("Log2Interceptor:after {} invoke", methodName);
        return ret;
    }
}