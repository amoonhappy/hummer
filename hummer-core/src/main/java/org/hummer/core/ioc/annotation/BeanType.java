package org.hummer.core.ioc.annotation;

public enum BeanType {
    HUMMER_BEAN(1),
    SPRING_BEAN(2),
    MAPPER_BEAN(3);

    private final int value;

    BeanType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
