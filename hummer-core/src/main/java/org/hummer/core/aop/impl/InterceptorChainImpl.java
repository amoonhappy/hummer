package org.hummer.core.aop.impl;

import org.hummer.core.aop.intf.Interceptor;
import org.hummer.core.aop.intf.InterceptorChain;
import org.hummer.core.exception.AopInvocationException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class InterceptorChainImpl implements InterceptorChain {

    protected final Object proxy;
    protected final Method method;
    final Object target;
    private final Class targetClass;
    Object[] arguments;
    private LinkedList<Interceptor> chain;
    private int curIndex = -1;

    /**
     * Construct a new ReflectiveMethodInvocation with the given arguments.
     *
     * @param proxy       the proxy object that the invocation was made on
     * @param target      the target object to invoke
     * @param method      the method to invoke
     * @param arguments   the arguments to invoke the method with
     * @param targetClass the target class, for MethodMatcher invocations
     * @param chain       interceptors that should be applied, along with any
     *                    InterceptorAndDynamicMethodMatchers that need evaluation at
     *                    runtime. MethodMatchers included in this struct must already
     *                    have been found to have matched as far as was possibly
     *                    statically. Passing an array might be about 10% faster, but
     *                    would complicate the code. And it would work only for static
     *                    pointcuts.
     */
    InterceptorChainImpl(Object proxy, Object target, Method method, Object[] arguments, Class targetClass,
                         LinkedList<Interceptor> chain) {
        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        this.chain = chain;
    }

    /**
     * Invoke the given target via reflection, as part of an AOP method
     * invocation.
     *
     * @param target the target object
     * @param method the method to invoke
     * @param args   the arguments for the method
     * @return the invocation result, if any
     * @throws Throwable                                      if thrown by the target method
     * @throws AopInvocationException                         throw if Hummer's AOP is failed
     */
    private static Object invokeJoinPointUsingReflection(Object target, Method method, Object[] args) throws Throwable {

        // Use reflection to invoke the method.
        try {
            if (!Modifier.isPublic(method.getModifiers())
                    || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
                method.setAccessible(true);
            }
            return method.invoke(target, args);
        } catch (InvocationTargetException ex) {
            // Invoked method threw a checked exception.
            // We must rethrow it. The client won't see the interceptor.
            throw ex.getTargetException();
        } catch (IllegalArgumentException ex) {
            throw new AopInvocationException("AOP configuration seems to be invalid: tried calling method [" + method
                    + "] on target [" + target + "]", ex);
        } catch (IllegalAccessException ex) {
            throw new AopInvocationException("Could not access method [" + method + "]", ex);
        }
    }

    public final Object getProxy() {
        return this.proxy;
    }

    public final Object getThis() {
        return this.target;
    }

    public final AccessibleObject getStaticPart() {
        return this.method;
    }

    /**
     * Return the method invoked on the proxied interface. May or may not
     * correspond with a method invoked on an underlying implementation of that
     * interface.
     */
    public final Method getMethod() {
        return this.method;
    }

    public final Object[] getArguments() {
        return (this.arguments != null ? this.arguments : new Object[0]);
    }

    public Object proceed() throws Throwable {
        // We start with an index of -1 and increment early.
        if (this.curIndex == this.chain.size() - 1) {
            return invokeJoinPoint();
        }

        Object interceptorOrInterceptionAdvice = this.chain.get(++this.curIndex);

        if (interceptorOrInterceptionAdvice != null) {
            if (interceptorOrInterceptionAdvice instanceof Perl5DynamicMethodInterceptor) {
                // Evaluate dynamic method matcher here: static part will
                // already have
                // been evaluated and found to match.
                Perl5DynamicMethodInterceptor dm = (Perl5DynamicMethodInterceptor) interceptorOrInterceptionAdvice;
                if (dm.matches(this.method, this.targetClass)) {
                    return dm.invoke(this);
                } else {
                    // Dynamic matching failed.
                    // Skip this interceptor and invoke the next in the chain.
                    return proceed();
                }
            } else {
                // It's an interceptor, so we just invoke it: The pointcut will
                // have
                // been evaluated statically before this object was constructed.
                return ((Interceptor) interceptorOrInterceptionAdvice).invoke(this);
            }
        } else {
            return invokeJoinPoint();
        }
    }

    /**
     * Invoke the joinpoint using reflection. Subclasses can override this to
     * use custom invocation.
     *
     * @return the return value of the joinpoint
     * @throws Throwable if invoking the joinpoint resulted in an exception
     */
    protected Object invokeJoinPoint() throws Throwable {
        return invokeJoinPointUsingReflection(this.target, this.method, this.arguments);
    }

    public int getCurrentIndex() {
        return curIndex;
    }

    public boolean hasNext() {
        return chain.get(curIndex + 1) != null;
    }

    public boolean isEmpty() {
        return (chain == null || chain.size() > 0);
    }

    public Interceptor next() {
        return chain.get(curIndex++);
    }

    public void addInterceptor(Interceptor in, int index) {
        chain.add(index, in);
    }

    public void addInterceptor2First(Interceptor in) {
        chain.addFirst(in);
    }

    public void addInterceptor2Last(Interceptor in) {
        chain.addLast(in);
    }
}
