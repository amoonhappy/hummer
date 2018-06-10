package org.hummer.core.factory.impl;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.apache.commons.beanutils.BeanUtils;
import org.hummer.core.aop.impl.InterceptorChainCGLibCallback;
import org.hummer.core.aop.intf.Interceptor;
import org.hummer.core.config.impl.CPConfigManager;
import org.hummer.core.config.intf.IXMLBeanConfig;
import org.hummer.core.config.intf.IXMLConfiguration;
import org.hummer.core.exception.BeanException;
import org.hummer.core.exception.NoBeanDefinationException;
import org.hummer.core.factory.intf.IBeanFactory;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class XMLBeanFactory implements IBeanFactory {
    // ConfigManager cm = ConfigManager.getInstance();
    private static Logger log = Log4jUtils.getLogger(XMLBeanFactory.class);
    private static Map<String, IXMLConfiguration> singletonBeanConfigCache = CPConfigManager.getInstance()
            .getAllXMLConfiguration();
    private static IBeanFactory instance = new XMLBeanFactory();
    private Map<String, Object> singletonBeanCache = new HashMap<>();
    private LinkedList<Interceptor> ic = new LinkedList<>();

    private XMLBeanFactory() {
        initialBeanObject();
        // use aop xml configuration attribute enabledBeanIds to register
        // interceptors
        initialInterceptors();

        // ic.add((Interceptor) getBean("transactionInterceptor"));
        // ic.add((Interceptor) getBean("performanceLog"));
    }

    private void initialInterceptors() {
        Map ids = (Map) getBean("enabledBeanIds");
        if (ids != null) {
            String enabledBeanIds = (String) ids.get("enabledBeanIds");
            String[] enabledIcs; //= StringUtil.joinArray(enabledBeanIds, ",");
            log.info("enabledBeanIds: [{}]", enabledBeanIds);
            enabledIcs = StringUtil.joinArray(enabledBeanIds, ",");
            //StringUtils.delimitedListToStringArray(enabledBeanIds, ",", " ");

            for (int i = 0; i < enabledIcs.length; i++) {
                ic.add((Interceptor) getBean(enabledIcs[i]));
                log.info("add Interceptor: [{}], Seq: [{}]", enabledIcs[i], i);
            }
        } else {
            log.info("no interceptors loaded, pls check hummer aop xml configuration!");
        }
    }

    public static IBeanFactory getInstance() {
        return instance;
    }

    public void reInit() {
        singletonBeanConfigCache = CPConfigManager.getInstance().getAllXMLConfiguration();
        singletonBeanCache = new HashMap<>();
        ic = new LinkedList<>();
        initialBeanObject();
        initialInterceptors();
    }

    private void initialBeanObject() {
        if (singletonBeanConfigCache != null) {
            Set<String> beanIds = singletonBeanConfigCache.keySet();
            for (Iterator<String> it = beanIds.iterator(); it.hasNext(); ) {
                String beanId = it.next();
                try {
                    //when the bean is defined as singleton, then put to the bean cache
                    if (this.isSingleton(beanId)) {
                        singletonBeanCache.put(beanId, beanConfig2BeanObject(singletonBeanConfigCache.get(beanId)));
                    } else {
                        // if not singleton, the bean should be created during access
                        log.info("bean [{}] is not singleton!", beanId);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("init Bean Objects failed!", e);
                }
            }
        }

    }

    public boolean containsBean(String name) {
        return singletonBeanCache.keySet().contains(name);
    }

    public Object getBean(String name) throws BeanException {
        Object ret = null;
        if (!StringUtil.isEmpty(name)) {
            ret = singletonBeanCache.get(name);
            if (ret == null) {
                try {
                    log.info("bean: [{}] is not found in singleton bean cache, try to create new one", name);
                    return beanConfig2BeanObject(singletonBeanConfigCache.get(name));
                } catch (Exception e) {
                    log.info("bean: [{}]  is not properly defined in the bean xml, pls try to check the xml configuration!", name);
                    return null;
                }
            }
        }
        return ret;
    }

    public Object getBean(String name, Class requiredType) throws BeanException {
        Object ret = null;
        if (!StringUtil.isEmpty(name)) {
            ret = singletonBeanCache.get(name);
        }

        assert ret != null;
        if (!ret.getClass().getSimpleName().equals(requiredType.getSimpleName())) {
            final String notMatch = "the class: " + requiredType.getSimpleName() + " type don't match the bean id: "
                    + name;
            log.error(notMatch);
            throw new BeanException(notMatch);
        }
        return ret;
    }

    public Class getType(String name) throws NoBeanDefinationException {
        return this.getBean(name).getClass();
    }

    public boolean isSingleton(String name) throws NoBeanDefinationException {
        return ((IXMLBeanConfig) singletonBeanConfigCache.get(name)).isSingleton();
    }

    public boolean isTypeMatch(String name, Class targetType) throws NoBeanDefinationException {
        Object ret = getBean(name);
        return ret.getClass().getSimpleName().equals(targetType.getSimpleName());
    }

    private Object beanConfig2BeanObject(IXMLConfiguration configuration)
            throws IllegalAccessException, InvocationTargetException {
        Object obj = null;

        if (configuration instanceof IXMLBeanConfig) {
            IXMLBeanConfig beanConfig = (IXMLBeanConfig) configuration;
            Class classImpl = beanConfig.getBeanClass();
            if (classImpl != null) {
                obj = getProxy(classImpl);

                Map<String, String> refBeanIds = beanConfig.getProp2RefBeanIdMapping();
                Map<String, String> propertiesValue = beanConfig.getXMLProperteisValueMapping();

                if (propertiesValue != null) {
                    for (Iterator<String> it = propertiesValue.keySet().iterator(); it.hasNext(); ) {
                        String propertiesName = it.next();
                        String value = propertiesValue.get(propertiesName);
                        BeanUtils.setProperty(obj, propertiesName, value);
                    }
                }

                if (refBeanIds != null) {
                    for (Iterator<String> it = refBeanIds.keySet().iterator(); it.hasNext(); ) {
                        String propertiesName = it.next();
                        if (singletonBeanCache.get(refBeanIds.get(propertiesName)) != null) {
                            BeanUtils.setProperty(obj, propertiesName,
                                    singletonBeanCache.get(refBeanIds.get(propertiesName)));
                        } else {
                            BeanUtils.setProperty(obj, propertiesName, beanConfig2BeanObject(
                                    singletonBeanConfigCache.get(refBeanIds.get(propertiesName))));
                        }
                        return obj;
                    }
                }
            } else {
                return beanConfig.getXMLProperteisValueMapping();
            }
        }
        return obj;
    }

    private Object getProxy(Class clazz) {
        Object ret = null;
        String simpleName = clazz.getSimpleName();
        try {
            if (clazz.isInterface()) {
                final String interfaceErrorMsg = "could not create the proxy for interface:" + simpleName;
                log.error(interfaceErrorMsg);
                throw new RuntimeException(interfaceErrorMsg);
            }
            ret = clazz.newInstance();
            // if the object is already an interceptor, return;
            if (ret instanceof Interceptor) {
                return ret;
            }
            // ret = new Object();

            Enhancer eh = new Enhancer();
            InterceptorChainCGLibCallback callback = new InterceptorChainCGLibCallback(ic, ret, clazz);
            Callback[] callbacks = new Callback[]{callback, NoOp.INSTANCE};
            eh.setSuperclass(clazz);
            eh.setCallbackFilter(callback.getFilter());

            eh.setCallbacks(callbacks);
            ret = eh.create();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("error when initial the proxy of instance: " + simpleName, e);
        } catch (Throwable e) {
            log.error("error when enhance the class of instance via CGLIB: " + simpleName, e);
            throw new RuntimeException("could not create the proxy for class :" + simpleName);
        }
        return ret;
    }
}
