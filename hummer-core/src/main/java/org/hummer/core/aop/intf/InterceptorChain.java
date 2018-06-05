package org.hummer.core.aop.intf;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Hummer Framework's AOP interface extends aopalliance
 *
 * @author jeff.zhou
 */
public interface InterceptorChain extends MethodInvocation {
    boolean isEmpty();

    boolean hasNext();

    int getCurrentIndex();

    Interceptor next();

    void addInterceptor2First(Interceptor in);

    void addInterceptor2Last(Interceptor in);

    void addInterceptor(Interceptor in, int index);
}
