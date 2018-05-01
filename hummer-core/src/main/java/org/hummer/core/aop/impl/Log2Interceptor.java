package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class Log2Interceptor extends Perl5DynamicMethodInterceptor {
    public Log2Interceptor(String[] patterns, String[] excludedPatterns) {
        super.setPatterns(patterns);
        super.setExcludedPatterns(excludedPatterns);
    }

    public Object invoke(MethodInvocation arg0) throws Throwable {
        System.out.println("Log2Proxy:before " + arg0.getMethod().getName() + " invoke");
        Object ret = arg0.proceed();
        System.out.println("Log2Proxy:after " + arg0.getMethod().getName() + " invoke");
        return ret;
    }
}