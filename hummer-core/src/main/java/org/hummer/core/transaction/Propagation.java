package org.hummer.core.transaction;

public enum Propagation {
    //must in a transaction
    REQUIRED(0),
    //can be in a transaction
    SUPPORTS(1),
    //must in a transaction
    MANDATORY(2),
    //must in a new transaction
    REQUIRES_NEW(3),
    //cannot be in a transaction
    NOT_SUPPORTED(4),
    NEVER(5),
    NESTED(6);

    private final int value;

    private Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
