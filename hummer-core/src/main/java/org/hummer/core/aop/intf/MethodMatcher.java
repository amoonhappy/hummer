package org.hummer.core.aop.intf;

import java.lang.reflect.Method;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public interface MethodMatcher {
    boolean matches(Method method, Class targetClass);
}
