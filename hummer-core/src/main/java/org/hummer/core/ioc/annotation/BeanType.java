package org.hummer.core.ioc.annotation;

public enum BeanType {
    HUMMER_BEAN(1),
    SPRING_BEAN(2);

    private final int value;

    BeanType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
