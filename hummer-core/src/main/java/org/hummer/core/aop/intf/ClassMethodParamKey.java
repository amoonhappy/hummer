package org.hummer.core.aop.intf;

import org.apache.commons.lang3.StringUtils;
import org.hummer.core.util.Assert;

import java.io.Serializable;
import java.util.Arrays;

public class ClassMethodParamKey implements Serializable {
    public static final SimpleKey EMPTY = new SimpleKey(new Object[0]);
    private final Object[] params;
    private final int hashCode;

    public ClassMethodParamKey(String classSimpleName, String methodName, Object... elements) {
        Assert.notNull(classSimpleName, "classSimpleName must not be null");
        Assert.notNull(methodName, "methodName must not be null");
        Assert.notNull(elements, "Elements must not be null");
        this.params = new Object[elements.length];
        System.arraycopy(elements, 0, this.params, 0, elements.length);
        this.hashCode = Arrays.deepHashCode(this.params);
    }

    public boolean equals(Object obj) {
        return this == obj || obj instanceof ClassMethodParamKey && Arrays.deepEquals(this.params, ((ClassMethodParamKey) obj).params);
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        return this.getClass().getSimpleName() + " [" + StringUtils.join(this.params, ',') + "]";
    }
}
