package org.hummer.core.factory.impl;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.hummer.core.aop.impl.InterceptorChainCGLibCallback;
import org.hummer.core.aop.intf.Interceptor;
import org.hummer.core.factory.intf.IProxyFactory;
import org.hummer.core.util.Log4jUtils;

import java.util.LinkedList;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class Cglib2ProxyFactory implements IProxyFactory {
    private static Logger log = Log4jUtils.getLogger(Cglib2ProxyFactory.class);

    private static IProxyFactory instance = new Cglib2ProxyFactory();

    LinkedList<Interceptor> ic = new LinkedList<Interceptor>();

    private Cglib2ProxyFactory() {
    }

    public static IProxyFactory getInstance() {
        return instance;
    }

    public Object getProxy(Class clazz) {
        ic.add((Interceptor) XMLBeanFactory.getInstance().getBean("transactionIntercepter"));
        Object ret = null;
        String simpleName = clazz.getSimpleName();
        try {
            if (clazz.isInterface()) {
                final String interfaceErrorMsg = "could not create the proxy for interface:" + simpleName;
                log.error(interfaceErrorMsg);
                throw new RuntimeException(interfaceErrorMsg);
            }
            ret = clazz.newInstance();
            // ret = newweb Object();

            Enhancer eh = new Enhancer();
            InterceptorChainCGLibCallback callback = new InterceptorChainCGLibCallback(ic, ret, clazz);
            Callback[] callbacks = new Callback[]{callback, NoOp.INSTANCE};
            eh.setSuperclass(clazz);
            eh.setCallbackFilter(callback.getFilter());

            eh.setCallbacks(callbacks);
            ret = eh.create();
            try {
                BeanUtils.setProperty(ret, "proxy", ret);
                if (log.isDebugEnabled()) {
                    try {
                        Object temp = BeanUtils.getProperty(ret, "proxy");
                        log.debug("successfully set the proxy properties " + temp + "of instance: " + simpleName);
                    } catch (NoSuchMethodException e) {
                        log.warn("could not found proxy properties of instance: " + simpleName, e);
                    }
                }
            } catch (Exception e) {
                log.error("error when set the proxy properties of instance: " + simpleName, e);
            }
        } catch (InstantiationException e) {
            log.error("error when initial the proxy of instance: " + simpleName, e);
        } catch (IllegalAccessException e) {
            log.error("error when initial the proxy of instance: " + simpleName, e);
        } catch (Throwable e) {
            log.error("error when enhance the class of instance via CGLIB2: " + simpleName, e);
            throw new RuntimeException("could not create the proxy for class :" + simpleName);
        }
        return ret;
    }
}
