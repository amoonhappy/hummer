package org.hummer.core.util;

public class ClassLoaderUtil {

    public static Class findClass(String className) throws ClassNotFoundException {
        Class ret = null;
        if (!StringUtil.isEmpty(className)) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            ret = cl.loadClass(className);
        }
        return ret;
    }

}
