package org.hummer.core.transaction.exceptions;

public class TransactionUsageException extends TransactionException {

    /**
     * Constructor for TransactionUsageException.
     *
     * @param msg the detail message
     */
    public TransactionUsageException(String msg) {
        super(msg);
    }

    /**
     * Constructor for TransactionUsageException.
     *
     * @param msg   the detail message
     * @param cause the root cause from the transaction API in use
     */
    public TransactionUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }

}