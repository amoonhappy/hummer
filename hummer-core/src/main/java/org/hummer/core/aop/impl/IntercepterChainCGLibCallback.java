package org.hummer.core.aop.impl;

import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.aop.intf.Interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class IntercepterChainCGLibCallback implements MethodInterceptor, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4000967288710540884L;

    private final LinkedList<Interceptor> adviceChain;

    private final Object target;

    private final Class targetClass;

    public IntercepterChainCGLibCallback(LinkedList<Interceptor> adviceChain, Object target, Class targetClass) {
        this.adviceChain = adviceChain;
        this.target = target;
        this.targetClass = targetClass;
    }

    private static Object massageReturnTypeIfNecessary(Object proxy, Object target, Method method, Object retVal) {
        // Massage return value if necessary
        if (retVal != null && retVal == target) {
            retVal = proxy;
        }
        return retVal;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object retVal = null;
        MethodInvocation invocation = new CglibMethodInvocation(proxy, this.target, method, args, this.targetClass,
                this.adviceChain, methodProxy);
        // If we get here, we need to create a MethodInvocation.
        retVal = invocation.proceed();
        retVal = massageReturnTypeIfNecessary(proxy, this.target, method, retVal);
        return retVal;
    }

    public CallbackFilter getFilter() {
        return new DynamicMethodFilter();
    }

    class DynamicMethodFilter implements CallbackFilter {
        public int accept(Method arg0) {
            return 0;
        }
    }

}
