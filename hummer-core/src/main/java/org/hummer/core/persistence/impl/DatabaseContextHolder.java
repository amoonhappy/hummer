package org.hummer.core.persistence.impl;

public class DatabaseContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDSType(String customerType) {
        contextHolder.set(customerType);
    }

    public static String getDSType() {
        return contextHolder.get();
    }

    public static void clearDSType() {
        contextHolder.remove();
    }
}
