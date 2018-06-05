package org.hummer.core.aop.intf;

import java.lang.reflect.Method;

/**
 * Used to determine whether a method should be wrapped by CGLIB proxy for AOP
 *
 * @author jeff.zhou
 */
public interface MethodMatcher {
    boolean matches(Method method, Class targetClass);
}
