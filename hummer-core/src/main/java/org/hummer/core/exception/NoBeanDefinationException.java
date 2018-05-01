package org.hummer.core.exception;

public class NoBeanDefinationException extends BeanException {

    /**
     *
     */
    private static final long serialVersionUID = 1738573547287047876L;

    /**
     * Name of the missing bean
     */
    private String beanName;

    /**
     * Required bean type
     */
    private Class beanType;

    public NoBeanDefinationException(String name) {
        super("No bean named '" + name + "' is defined");
        this.beanName = name;
    }

    /**
     * Create a newweb NoBeanDefinationException.
     *
     * @param name    the name of the missing bean
     * @param message further, detailed message describing the problem
     */
    public NoBeanDefinationException(String name, String message) {
        super("No bean named '" + name + "' is defined: " + message);
        this.beanName = name;
    }

    /**
     * Create a newweb NoBeanDefinationException.
     *
     * @param type    required type of bean
     * @param message further, detailed message describing the problem
     */
    public NoBeanDefinationException(Class type, String message) {
        super("No unique bean of type [" + type.getName() + "] is defined: " + message);
        this.beanType = type;
    }

    /**
     * Return the name of the missing bean, if it was a lookup by name that
     * failed.
     */
    public String getBeanName() {
        return this.beanName;
    }

    /**
     * Return the required type of bean, if it was a lookup by type that failed.
     */
    public Class getBeanType() {
        return this.beanType;
    }
}
