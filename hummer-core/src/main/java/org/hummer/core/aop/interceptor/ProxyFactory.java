package org.hummer.core.aop.interceptor;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.apache.commons.beanutils.BeanUtils;
import org.hummer.core.aop.impl.InterceptorChainCGLibCallback;
import org.hummer.core.aop.impl.Perl5DynamicMethodInterceptor;
import org.hummer.core.aop.intf.Interceptor;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class ProxyFactory {
    private static ProxyFactory instance = new ProxyFactory();
    private static Logger log = Log4jUtils.getLogger(Perl5DynamicMethodInterceptor.class);

    private LinkedList<Interceptor> ic = new LinkedList<>();

    private ProxyFactory() {
        ic.addFirst(new TransactionInterceptor());
        // ic.addLast(new Log2Interceptor(
        // new String[] { "^[a-z|A-Z|.|0-9]+Service.print[0-9]*" },
        // new String[] { "^get", "^set", "^toString" }));
    }

    public static ProxyFactory getInstance() {
        return instance;
    }

    public Object getProxy(Class clazz) {
        Object ret = null;
        try {
            if (clazz.isInterface()) {
                throw new RuntimeException("could not create the proxy for interface");
            }
            ret = clazz.newInstance();
            // ret = new Object();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("get Proxy error!", e);
        }
        Enhancer eh = new Enhancer();
        InterceptorChainCGLibCallback callback = new InterceptorChainCGLibCallback(ic, ret, clazz);
        Callback[] callbacks = new Callback[]{callback, NoOp.INSTANCE};
        eh.setSuperclass(clazz);
        eh.setCallbackFilter(callback.getFilter());

        eh.setCallbacks(callbacks);
        ret = eh.create();
        try {
            BeanUtils.setProperty(ret, "test", ret);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Cglib Enhancement error!", e);
        }
        return ret;
    }

}
