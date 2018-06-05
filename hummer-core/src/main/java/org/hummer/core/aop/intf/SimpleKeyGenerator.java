package org.hummer.core.aop.intf;

import java.lang.reflect.Method;

/**
 * Simple Redis Cache Key Generator
 */
public class SimpleKeyGenerator implements KeyGenerator {
    public SimpleKeyGenerator() {
    }

    public static Object generateKey(Object... params) {
        if (params.length == 0) {
            return SimpleKey.EMPTY;
        } else {
            if (params.length == 1) {
                Object param = params[0];
                if (param != null && !param.getClass().isArray()) {
                    return param;
                }
            }

            return new SimpleKey(params);
        }
    }

    public Object generate(Object target, Method method, Object... params) {
        return generateKey(params);
    }
}
