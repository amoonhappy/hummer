package org.hummer.core.aop.intf;

import org.aopalliance.intercept.MethodInvocation;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public interface InterceptorChain extends MethodInvocation {
    public boolean isEmpty();

    public boolean hasNext();

    public int getCurrentIndex();

    public Interceptor next();

    public void addInterceptor2First(Interceptor in);

    public void addInterceptor2Last(Interceptor in);

    public void addInterceptor(Interceptor in, int index);
}
