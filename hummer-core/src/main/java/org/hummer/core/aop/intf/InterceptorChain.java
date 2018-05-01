package org.hummer.core.aop.intf;

import org.aopalliance.intercept.MethodInvocation;

/**
 * iFOP Spacee for Developer Party
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
