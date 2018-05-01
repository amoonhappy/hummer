package org.hummer.core.aop.impl;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.apache.commons.beanutils.BeanUtils;
import org.hummer.core.aop.intf.Interceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class ProxyFactory {
    private static ProxyFactory instance = new ProxyFactory();

    LinkedList<Interceptor> ic = new LinkedList<Interceptor>();

    private ProxyFactory() {
        ic.addFirst(new TransactionInterceptor());
        // ic.addLast(newweb Log2Interceptor(
        // newweb String[] { "^[a-z|A-Z|.|0-9]+Service.print[0-9]*" },
        // newweb String[] { "^get", "^set", "^toString" }));
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
            // ret = newweb Object();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Enhancer eh = new Enhancer();
        IntercepterChainCGLibCallback callback = new IntercepterChainCGLibCallback(ic, ret, clazz);
        Callback[] callbacks = new Callback[]{callback, NoOp.INSTANCE};
        eh.setSuperclass(clazz);
        eh.setCallbackFilter(callback.getFilter());

        eh.setCallbacks(callbacks);
        ret = eh.create();
        try {
            BeanUtils.setProperty(ret, "test", ret);
            // Object temp = BeanUtils.getProperty(ret, "test");
            // System.out.println(temp);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // } catch (NoSuchMethodException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return ret;
    }

}
