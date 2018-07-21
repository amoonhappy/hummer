package org.hummer.core.util;

import org.hummer.core.factory.impl.HummerBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class HummerUtil {
    public static <T> T getBean(String beanName, final Class<T> requiredType) {
        return (T) HummerBeanFactory.getInstance().getBean(beanName);
    }

    public static Field[] getDeepFieldByName(Class beanClass, String fieldName) {
        Assert.notNull(fieldName, "field can not be null");
        Assert.notNull(beanClass, "bean Class can not be null");
        Field[] ret = null;
        List<Field> allFields = new LinkedList<>();
        ret = getDeepFields(beanClass);
        if (ret.length > 0) {
            for (Field field : ret) {
                String tempFieldName = field.getName();
                if (fieldName.equals(tempFieldName)) {
                    allFields.add(field);
                }
            }
        }
        return allFields.toArray(new Field[0]);

//        for (; beanClass != Object.class; beanClass = beanClass.getSuperclass()) {
//            Field[] test = beanClass.getDeclaredFields();
//            for (Field field : test) {
//                int mod = field.getModifiers();
//                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
//                    continue;
//                }
//                if (fieldName.equals(field.getName())) {
//                    allFields.add(field);
//                }
//            }
//        }

    }

    public static Field[] getDeepFields(Class beanClass) {
        List<Field> allFields = new LinkedList<>();
        for (; beanClass != Object.class; beanClass = beanClass.getSuperclass()) {
            Field[] test = beanClass.getDeclaredFields();
            for (Field field : test) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                allFields.add(field);
            }
        }
        return allFields.toArray(new Field[0]);
    }
}
