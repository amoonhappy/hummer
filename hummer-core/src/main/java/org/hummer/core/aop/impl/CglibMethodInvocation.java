package org.hummer.core.aop.impl;

import net.sf.cglib.proxy.MethodProxy;
import org.hummer.core.aop.intf.Interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;

/**
 * iFOP Space for Developer Party
 *
 * @author jeff.zhou
 */
public class CglibMethodInvocation extends InterceptorChainImpl {

    private MethodProxy methodProxy;

    private boolean protectedMethod;

    public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] args, Class targetClass,
                                 LinkedList<Interceptor> adviceChain, MethodProxy methodProxy) {
        super(proxy, target, method, args, targetClass, adviceChain);
        this.methodProxy = methodProxy;
        this.protectedMethod = Modifier.isProtected(method.getModifiers());
    }

    /**
     * Gives a marginal performance improvement versus using reflection to
     * invoke the target when invoking public methods.
     */
    protected Object invokeJoinpoint() throws Throwable {
        if (this.protectedMethod) {
            return super.invokeJoinpoint();
        } else {
            return this.methodProxy.invoke(this.target, this.arguments);
            // return super.invokeJoinpoint();
        }
    }
}
