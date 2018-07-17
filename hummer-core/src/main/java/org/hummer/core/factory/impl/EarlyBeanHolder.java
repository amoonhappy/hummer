package org.hummer.core.factory.impl;

/**
 * 装载目标对象和代理对象的工具类，通过二级Bean缓存和autowiring状态解决Bean之间循环依赖的问题
 *
 * @author Jeff Zhou
 * @version 1.0
 */
public class EarlyBeanHolder {
    private Object target;
    private Object proxy;
    private Class beanClass;
    private String beanId;
    private boolean autowiring = false;

    public boolean isAutowiring() {
        return autowiring;
    }

    public void setAutowiring(boolean autowiring) {
        this.autowiring = autowiring;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
