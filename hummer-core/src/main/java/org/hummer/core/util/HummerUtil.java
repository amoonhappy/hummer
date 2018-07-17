package org.hummer.core.util;

import org.hummer.core.factory.impl.HummerBeanFactory;

public class HummerUtil {
    public static <T> T getBean(String beanName, final Class<T> requiredType) {
        return (T) HummerBeanFactory.getInstance().getBean(beanName);
    }
}
