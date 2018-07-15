package org.hummer.core.factory.impl;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.apache.commons.beanutils.BeanUtils;
import org.hummer.core.aop.impl.InterceptorChainCGLibCallback;
import org.hummer.core.aop.intf.Interceptor;
import org.hummer.core.config.impl.CPConfigManager;
import org.hummer.core.config.intf.IXMLBeanConfig;
import org.hummer.core.config.intf.IXMLConfiguration;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.exception.BeanException;
import org.hummer.core.exception.NoBeanDefinationException;
import org.hummer.core.factory.intf.IBeanFactory;
import org.hummer.core.ioc.annotation.Autowired;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.MybatisUtil;
import org.hummer.core.util.ReflectionUtil;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class XMLBeanFactory implements IBeanFactory {
    // ConfigManager cm = ConfigManager.getInstance();
    private static Logger log = Log4jUtils.getLogger(XMLBeanFactory.class);
    private static Map<String, IXMLConfiguration> singletonBeanConfigCache = CPConfigManager.getInstance()
            .getAllXMLConfiguration();
    private static IBeanFactory instance = new XMLBeanFactory();
    private Map<String, Object> singletonBeanCache = new HashMap<>();
    private Map<String, Object> springBeanCache = new HashMap<>();
    private Map<String, Object> mapperCache = new HashMap<>();
    private LinkedList<Interceptor> ic = new LinkedList<>();

    private XMLBeanFactory() {
        initialBeanObject();
        // use aop xml configuration attribute enabledBeanIds to register
        // interceptors
        initialInterceptors();
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
        Object[] obj = null;
        Object target = null;
        Object proxy = null;
        if (configuration instanceof IXMLBeanConfig) {
            IXMLBeanConfig beanConfig = (IXMLBeanConfig) configuration;
            Class classImpl = beanConfig.getBeanClass();
            boolean isAutowired = beanConfig.isAutowired();
            /**
             * consider don't support xml config for bean initiation
             */
            if (classImpl != null && !isAutowired) {
                obj = getProxy(classImpl);
                target = obj[0];
                proxy = obj[1];
                Map<String, String> refBeanIds = beanConfig.getProp2RefBeanIdMapping();
                Map<String, String> propertiesValue = beanConfig.getXMLProperteisValueMapping();
                Map<String, String> springBeanIds = beanConfig.getProp2SpringBeanIdMapping();
                if (propertiesValue != null) {
                    for (Iterator<String> it = propertiesValue.keySet().iterator(); it.hasNext(); ) {
                        String propertiesName = it.next();
                        String value = propertiesValue.get(propertiesName);
                        BeanUtils.setProperty(target, propertiesName, value);
                        //BeanUtils.setProperty(proxy, propertiesName, value);
                    }
                }
                // get beans from Spring container
                if (springBeanIds != null) {
                    for (String propertiesName : springBeanIds.keySet()) {
                        String springBeanId = springBeanIds.get(propertiesName);
                        Object cachedSpringBeanValue = springBeanCache.get(springBeanId);
                        if (cachedSpringBeanValue == null) {
                            cachedSpringBeanValue = HummerContainer.getInstance().getBeanFromSpring(springBeanId);
                            this.springBeanCache.put(springBeanId, cachedSpringBeanValue);
                        }
                        injectBeanProperties(target, propertiesName, cachedSpringBeanValue);
                    }
                }
                if (refBeanIds != null) {
                    for (Iterator<String> it = refBeanIds.keySet().iterator(); it.hasNext(); ) {
                        String propertiesName = it.next();
                        Object cachedPropertiesValue = singletonBeanCache.get(refBeanIds.get(propertiesName));
                        IXMLConfiguration cachedPropertiesConfigValue =
                                singletonBeanConfigCache.get(refBeanIds.get(propertiesName));
                        if (cachedPropertiesValue != null) {
                            //inject reference bean from cached value
                            injectBeanProperties(target, propertiesName, cachedPropertiesValue);
                            //injectBeanProperties(proxy, propertiesName, cachedPropertiesValue);

                        } else {
                            //inject reference bean from xml config cache
                            Object propertieValue = beanConfig2BeanObject(cachedPropertiesConfigValue);
                            injectBeanProperties(target, propertiesName, propertieValue);
                            //injectBeanProperties(proxy, propertiesName, propertieValue);
                        }
                        return proxy;
                    }
                }
                //自动装载Bean
                /**
                 * 考虑只支持Annotation注入Bean
                 */
            } else if (classImpl != null && isAutowired) {
                obj = getProxy(classImpl);
                target = obj[0];
                proxy = obj[1];
                Field[] fields = target.getClass().getDeclaredFields();
                if (fields == null || fields.length == 0) {
                    return proxy;
                } else {
                    for (Field field : fields) {
                        Autowired autowired = field.getAnnotation(Autowired.class);
                        //if Autowired annotation exist, and
                        if (autowired != null) {
                            String beanName = field.getName();
                            Class beanType = field.getType();
                            switch (autowired.value()) {
                                case HUMMER_BEAN:
                                    Object cachedPropertiesValue = singletonBeanCache.get(beanName);
                                    IXMLConfiguration cachedPropertiesConfig = singletonBeanConfigCache.get(beanName);
                                    if (cachedPropertiesValue != null) {
                                        //inject reference bean from cached value
                                        injectBeanProperties(target, beanName, cachedPropertiesValue);
                                        //injectBeanProperties(proxy, propertiesName, cachedPropertiesValue);
                                    } else {
                                        //inject reference bean from xml config cache
                                        Object propertieValue = beanConfig2BeanObject(cachedPropertiesConfig);
                                        injectBeanProperties(target, beanName, propertieValue);
                                        //injectBeanProperties(proxy, propertiesName, propertieValue);
                                    }
                                    continue;
                                case SPRING_BEAN:
                                    Object cachedSpringBeanValue = springBeanCache.get(beanName);
                                    if (cachedSpringBeanValue == null) {
                                        cachedSpringBeanValue = HummerContainer.getInstance().getBeanFromSpring(beanName);
                                        this.springBeanCache.put(beanName, cachedSpringBeanValue);
                                    }
                                    injectBeanProperties(target, beanName, cachedSpringBeanValue);
                                    continue;
                                case MAPPER_BEAN:
                                    //TODO
                                    Object cachedMapper = mapperCache.get(beanName);
                                    if (cachedMapper == null) {
                                        cachedMapper = MybatisUtil.getSession().getMapper(beanType);
                                        this.mapperCache.put(beanName, cachedMapper);
                                    }
                                    injectBeanProperties(target, beanName, cachedMapper);
                            }
                        }
                    }
                }

            } else {
                return beanConfig.getXMLProperteisValueMapping();
            }
        }
        return proxy;
    }

    /**
     * Don't need setter method to resolve the IOC of defined beans
     *
     * @param obj
     * @param propertiesName
     * @param cachedPropertiesValue
     * @throws IllegalAccessException
     */
    private void injectBeanProperties(Object obj, String propertiesName, Object cachedPropertiesValue) throws IllegalAccessException {
        Class objClass = obj.getClass();
        try {
            Field tobeInjectedField = objClass.getDeclaredField(propertiesName);
            ReflectionUtil.makeAccessible(tobeInjectedField);
            tobeInjectedField.set(obj, cachedPropertiesValue);
        } catch (NoSuchFieldException e) {
            log.error("bean[{}] don't have properties[{}]to be injected by object [{}]", obj, propertiesName, cachedPropertiesValue);
        }
//        BeanUtils.setProperty(obj, propertiesName, cachedPropertiesValue);
    }

    private Object[] getProxy(Class clazz) {
        Object[] ret = new Object[2];
        Object target = null;
        String simpleName = clazz.getSimpleName();
        try {
            if (clazz.isInterface()) {
                String interfaceErrorMsg = "could not create the proxy for interface:" + simpleName;
                log.error(interfaceErrorMsg);
                throw new RuntimeException(interfaceErrorMsg);
            }

            // get the default contractor for a bean class, if don't exist or is not accessible, throw NoSuchMethodException
            Constructor a = clazz.getConstructor();
            target = a.newInstance();
            // if the object is already an interceptor, return;
            if (target instanceof Interceptor) {
                ret[0] = target;
                ret[1] = target;
                return ret;
            }
            // ret = new Object();

            Enhancer eh = new Enhancer();
            InterceptorChainCGLibCallback callback = new InterceptorChainCGLibCallback(ic, target, clazz);
            Callback[] callbacks = new Callback[]{callback};
            eh.setSuperclass(clazz);
            eh.setCallbackFilter(callback.getFilter());

            eh.setCallbacks(callbacks);
            ret[0] = target;
            ret[1] = eh.create();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            log.error("error when initial the proxy of instance: {}, pls check the default constructor!", simpleName, e);
        } catch (Throwable e) {
            log.error("error when enhance the class of instance via CGLIB: {}", simpleName, e);
            throw new RuntimeException("could not create the proxy for class :" + simpleName);
        }
        return ret;
    }
}
