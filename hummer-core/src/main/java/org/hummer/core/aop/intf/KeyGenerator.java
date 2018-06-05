package org.hummer.core.aop.intf;

import java.lang.reflect.Method;

public interface KeyGenerator {
    Object generate(Object var1, Method var2, Object... var3);
}