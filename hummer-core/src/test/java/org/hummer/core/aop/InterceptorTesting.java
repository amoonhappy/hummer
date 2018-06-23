package org.hummer.core.aop;

import org.hummer.core.aop.interceptor.ProxyFactory;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class InterceptorTesting {

    /**
     * @param args
     */
    public static void main(String[] args) {

        ProxyFactory pf = ProxyFactory.getInstance();
        TestService ts = (TestService) pf.getProxy(TestService.class);
        ts.print();
    }
}