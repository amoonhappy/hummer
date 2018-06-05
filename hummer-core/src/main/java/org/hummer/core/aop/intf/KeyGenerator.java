package org.hummer.core.aop.intf;

import java.lang.reflect.Method;

/**
 * Simple Redis Key Generator Interface
 */
public interface KeyGenerator {
    Object generate(Object var1, Method var2, Object... var3);
}