package org.hummer.core.exception;

public class BeanException extends UnCheckedException {
    /**
     *
     */
    private static final long serialVersionUID = -4192637028646774061L;

    /**
     * Create a newweb BeansException with the specified message.
     *
     * @param msg the detail message
     */
    public BeanException(String msg) {
        super(msg);
    }

    /**
     * Create a newweb BeansException with the specified message and root cause.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public BeanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
