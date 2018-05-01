package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.hummer.core.util.Log4jUtils;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class Log2Interceptor extends Perl5DynamicMethodInterceptor {
    private static Logger log = Log4jUtils.getLogger(Log2Interceptor.class);

    public Object invoke(MethodInvocation arg0) throws Throwable {
        log.info("Log2Proxy:before " + arg0.getMethod().getName() + " invoke");
        Object ret = arg0.proceed();
        log.info("Log2Proxy:after " + arg0.getMethod().getName() + " invoke");
        return ret;
    }
}