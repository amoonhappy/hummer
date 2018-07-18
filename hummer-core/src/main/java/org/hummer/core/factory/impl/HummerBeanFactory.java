package org.hummer.core.factory.impl;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.hummer.core.aop.impl.InterceptorChainCGLibCallback;
import org.hummer.core.aop.intf.Interceptor;
import org.hummer.core.config.impl.HummerConfigManager;
import org.hummer.core.config.intf.IXMLBeanConfig;
import org.hummer.core.config.intf.IXMLConfig;
import org.hummer.core.container.HummerContainer;
import org.hummer.core.exception.BeanException;
import org.hummer.core.exception.NoBeanDefinationException;
import org.hummer.core.factory.intf.IBeanFactory;
import org.hummer.core.ioc.annotation.Autowired;
import org.hummer.core.util.*;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hummer Framework核心Bean工厂类
 * 用来初始化AOP，创建Bean代理，并通过Annotation声明自动为HummerBean提供依赖注入服务
 * 目前支持Spring Bean加载，Mybatis Mapper加载，以及Spring MVC Controller的反向加载
 *
 * @author Jeff Zhou
 * @version 1.0
 * @date ${YEAR}
 */
@SuppressWarnings("all")
public class HummerBeanFactory implements IBeanFactory {
    private static Logger log = Log4jUtils.getLogger(HummerBeanFactory.class);
    private Map<String, IXMLConfig> singletonBeanConfigCache = HummerConfigManager.getInstance()
            .getAllXMLConfiguration();
    private static HummerBeanFactory instance = new HummerBeanFactory();
    private Map<String, Object> singletonBeanCache = new ConcurrentHashMap<>();
    private Map<String, Object> springBeanCache = new ConcurrentHashMap<>();
    private Map<String, EarlyBeanHolder> earlySingletonObjects = new ConcurrentHashMap<>();
    private Map<String, Object> mapperCache = new ConcurrentHashMap<>();
    private LinkedList<Interceptor> ic = new LinkedList<>();
    private List<String> postAutowireSpringBeanNameList = new ArrayList<>();
    private ConcurrentHashMap<String, Object> postAutowireSpringBeanMap = new ConcurrentHashMap<>();
    private List<String> postAutowireSpringMVCBeanNameList = new ArrayList<>();
    private ConcurrentHashMap<String, Object> postAutowireSpringMVCBeanMap = new ConcurrentHashMap<>();
    private Date initTime;

    public Set<String> getHummerBeanList() {
        return singletonBeanCache.keySet();
    }

    private HummerBeanFactory() {
        Set<String> beanNames = singletonBeanConfigCache.keySet();
        // use aop xml configuration attribute enabledBeanIds to register
        // interceptors
        initialInterceptors(beanNames);
        earlyInitialBeans(beanNames);
        autowiringBeans();
        initTime = new Date();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Hummer Bean Factory: start up date [");
        stringBuilder.append(DateUtil.format(initTime, DateUtil.SDF_CN));
        stringBuilder.append("];");
        stringBuilder.append(" HashCode is [@");
        stringBuilder.append(Integer.toHexString(hashCode()));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /**
     * add Spring Bean Name to be autowired
     *
     * @param list
     */
    public void addPostAutowireSpringMVCBeanNameList(List<String> list) {
        if (list != null) {
            postAutowireSpringMVCBeanNameList.addAll(list);
        }
    }

    /**
     * set Spring Bean Name to be autowired
     *
     * @param list
     */
    public void setPostAutowireSpringMVCBeanNameList(List<String> list) {
        postAutowireSpringMVCBeanNameList.clear();
        if (list != null) {
            postAutowireSpringMVCBeanNameList.addAll(list);
        }
    }

    /**
     * add Spring Bean Object map
     *
     * @param map
     */
    public void addPostAutowireSpringMVCBeanMap(Map<String, Object> map) {
        if (map != null) {
            postAutowireSpringMVCBeanMap.putAll(map);
        }
    }

    /**
     * set Spring Bean Object map
     *
     * @param map
     */
    public void setPostAutowireSpringMVCBeanMap(Map<String, Object> map) {
        postAutowireSpringMVCBeanMap.clear();
        if (map != null) {
            postAutowireSpringMVCBeanMap.putAll(map);
        }
    }

    /**
     * add Spring Bean Name to be autowired
     *
     * @param list
     */
    public void addPostAutowireSpringBeanNameList(List<String> list) {
        if (list != null) {
            postAutowireSpringBeanNameList.addAll(list);
        }
    }

    /**
     * set Spring Bean Name to be autowired
     *
     * @param list
     */
    public void setPostAutowireSpringBeanNameList(List<String> list) {
        postAutowireSpringBeanNameList.clear();
        if (list != null) {
            postAutowireSpringBeanNameList.addAll(list);
        }
    }

    /**
     * add Spring Bean Object map
     *
     * @param map
     */
    public void addPostAutowireSpringBeanMap(Map<String, Object> map) {
        if (map != null) {
            postAutowireSpringBeanMap.putAll(map);
        }
    }

    /**
     * set Spring Bean Object map
     *
     * @param map
     */
    public void setPostAutowireSpringBeanMap(Map<String, Object> map) {
        postAutowireSpringBeanMap.clear();
        if (map != null) {
            postAutowireSpringBeanMap.putAll(map);
        }
    }

    /**
     * Post Hummer Bean Injection for Spring Beans in spring-admin.xml
     *
     * @return
     */
    public boolean postSpringBeanAutowire() {
        boolean ret = true;
        ret = processHummerBeanAutowireForSpring(postAutowireSpringBeanNameList, postAutowireSpringBeanMap);
        return ret;
    }

    /**
     * Post Hummer Bean Injection for Spring MVC Beans in spring-mvc.xml
     *
     * @return
     */
    public boolean postSpringMVCBeanAutowire() {
        boolean ret = true;
        ret = processHummerBeanAutowireForSpring(postAutowireSpringMVCBeanNameList, postAutowireSpringMVCBeanMap);
        return ret;
    }

    /**
     * autowire SpringBeans (annotated as {@link org.hummer.core.ioc.annotation.HummerPostAutowired})for fields that annotated as
     * {@link org.hummer.core.ioc.annotation.Autowired}
     *
     * @param list
     * @param map
     * @return
     */
    private boolean processHummerBeanAutowireForSpring(List<String> list, ConcurrentHashMap<String, Object> map) {
        if (CollectionUtils.isNotEmpty(list) && MapUtils.isNotEmpty(map))
            for (String springMVCBeanName : list) {
//                if (springMVCBeanName.equals("knPositionController")) {
//                    Object temp = map.get(springMVCBeanName);
//                }
                Object springMVCBeanObject = map.get(springMVCBeanName);
                Class beanClass = springMVCBeanObject.getClass();
                Field[] fields = beanClass.getDeclaredFields();
                if (fields == null || fields.length == 0) {
                } else {
                    for (Field field : fields) {
                        Autowired autowired = field.getAnnotation(Autowired.class);
                        //if Autowired annotation exist, and
                        if (autowired != null) {
                            String depBeanName = field.getName();
                            Class depBeanType = field.getType();
                            Object depBeanObject;
                            switch (autowired.value()) {
                                case HUMMER_BEAN:
                                    depBeanObject = singletonBeanCache.get(depBeanName);
                                    if (depBeanObject != null) {
                                        injectBeanProperties(springMVCBeanObject, depBeanName, depBeanObject);
                                    } else {
                                        log.warn("postSpringBeanAutowire failed, no Hummer Bean [{}] found for Spring Bean [{}]", depBeanName, springMVCBeanName);
                                    }
                                    continue;
                                case SPRING_BEAN:
                                    depBeanObject = springBeanCache.get(depBeanName);
                                    if (depBeanObject == null) {
                                        depBeanObject = HummerContainer.getInstance().getBeanFromSpring(depBeanName);
                                        springBeanCache.put(depBeanName, depBeanObject);
                                    }
                                    if (depBeanObject != null) {
                                        injectBeanProperties(springMVCBeanObject, depBeanName, depBeanObject);
                                    } else {
                                        log.warn("postSpringBeanAutowire failed, no Hummer Bean [{}] found for Spring Bean [{}]", depBeanName, springMVCBeanName);
                                    }
                                    continue;
                                case MAPPER_BEAN:
                                    depBeanObject = mapperCache.get(depBeanName);
                                    if (depBeanObject == null) {
                                        depBeanObject = getMybatisMapperProxy(depBeanType);
                                        mapperCache.put(depBeanName, depBeanObject);
                                    }
                                    if (depBeanObject != null) {
                                        injectBeanProperties(springMVCBeanObject, depBeanName, depBeanObject);
                                    } else {
                                        log.warn("no Hummer Bean [{}] found for Spring Bean [{}]", depBeanName, springMVCBeanName);
                                    }
                            }
                        }
                    }
                }
            }
        return true;
    }

    private void initialInterceptors(Set<String> beanNames) {
        Set<String> interceptorList = new HashSet<>();
        String enabledBeanIdsName = "enabledBeanIds";
        String[] enabledIcs;
        IXMLBeanConfig enabledBeanIdsConfig = (IXMLBeanConfig) singletonBeanConfigCache.get(enabledBeanIdsName);
        beanNames.remove(enabledBeanIdsName);
        Map<String, String> xmlProperties = enabledBeanIdsConfig.getXMLProperteisValueMapping();
        String enabledBeanIdsValue = xmlProperties.get(enabledBeanIdsName);
        enabledIcs = StringUtil.joinArray(enabledBeanIdsValue, ",");


        for (String beanName : beanNames) {
            try {
                IXMLBeanConfig beanConfig = (IXMLBeanConfig) singletonBeanConfigCache.get(beanName);
                Class beanClass = beanConfig.getBeanClass();
                if (Interceptor.class.isAssignableFrom(beanClass)) {
                    Object temp = beanClass.newInstance();
                    interceptorList.add(beanName);
                    Map<String, String> propertiesValue = beanConfig.getXMLProperteisValueMapping();
                    if (propertiesValue != null) {
                        for (Iterator<String> it = propertiesValue.keySet().iterator(); it.hasNext(); ) {
                            try {
                                String propertiesName = it.next();
                                String value = propertiesValue.get(propertiesName);
                                BeanUtils.setProperty(temp, propertiesName, value);
                                //BeanUtils.setProperty(proxy, propertiesName, value);
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                continue;
                            }
                        }
                    }
                    //
                    ic.add((Interceptor) temp);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                continue;
            }
        }
        beanNames.removeAll(interceptorList);
    }

//    private static void initialInterceptors() {
//        Map ids = (Map) getBean("enabledBeanIds");
//        if (ids != null) {
//            String enabledBeanIds = (String) ids.get("enabledBeanIds");
//            String[] enabledIcs; //= StringUtil.joinArray(enabledBeanIds, ",");
//            log.info("enabledBeanIds: [{}]", enabledBeanIds);
//            enabledIcs = StringUtil.joinArray(enabledBeanIds, ",");
//            //StringUtils.delimitedListToStringArray(enabledBeanIds, ",", " ");
//
//            for (int i = 0; i < enabledIcs.length; i++) {
//                ic.add((Interceptor) getBean(enabledIcs[i]));
//                log.info("add Interceptor: [{}], Seq: [{}]", enabledIcs[i], i);
//            }
//        } else {
//            log.info("no interceptors loaded, pls check hummer aop xml configuration!");
//        }
//    }

    public static HummerBeanFactory getInstance() {
        return instance;
    }

    public void reInit() {
        instance = new HummerBeanFactory();
        instance.postSpringBeanAutowire();
        instance.postSpringMVCBeanAutowire();
//        singletonBeanConfigCache = HummerConfigManager.getInstance().getAllXMLConfiguration();
//        singletonBeanCache = new HashMap<>();
//        earlySingletonObjects = new HashMap<>();
//        springBeanCache = new HashMap<>();
//        mapperCache = new HashMap<>();
//        ic = new LinkedList<>();
//        Set<String> beanNames = singletonBeanConfigCache.keySet();
//        // use aop xml configuration attribute enabledBeanIds to register
//        // interceptors
//        initialInterceptors(beanNames);
//        earlyInitialBeans(beanNames);
//        autowiringBeans();
    }

//    private void initialBeanObject() {
//        if (singletonBeanConfigCache != null) {
//            Set<String> beanIds = singletonBeanConfigCache.keySet();
//            for (Iterator<String> it = beanIds.iterator(); it.hasNext(); ) {
//                String beanId = it.next();
//                try {
//                    //when the bean is defined as singleton, then put to the bean cache
//                    if (this.isSingleton(beanId)) {
//                        singletonBeanCache.put(beanId, beanConfig2BeanObject(singletonBeanConfigCache.get(beanId)));
//                    } else {
//                        // if not singleton, the bean should be created during access
//                        log.info("bean [{}] is not singleton!", beanId);
//                    }
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    log.error("init Bean Objects failed!", e);
//                }
//            }
//        }
//
//    }

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
//                    return beanConfig2BeanObject(singletonBeanConfigCache.get(name));
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

    private void earlyInitialBeans(Set<String> beanNames) {
        //create proxy objects for all beans
        if (beanNames != null && beanNames.size() > 0) {
            for (String beanName : beanNames) {
                IXMLBeanConfig beanConfig = (IXMLBeanConfig) singletonBeanConfigCache.get(beanName);
                Class beanClass = beanConfig.getBeanClass();
                if (beanClass != null) {
                    if (earlySingletonObjects.get(beanName) == null) {
                        EarlyBeanHolder earlyBeanHolder = new EarlyBeanHolder();
                        earlyBeanHolder.setBeanClass(beanClass);
                        earlyBeanHolder.setBeanId(beanName);
                        earlyBeanHolder = getProxy(earlyBeanHolder);
                        earlySingletonObjects.put(beanName, earlyBeanHolder);
                    }
                }
            }
        }
    }

    private void autowiringBeans() {
        Set<String> dependencies = earlySingletonObjects.keySet();
        for (String beanName : dependencies) {
            EarlyBeanHolder beanHolder = earlySingletonObjects.get(beanName);
            IXMLBeanConfig beanConfig = (IXMLBeanConfig) singletonBeanConfigCache.get(beanName);
            singletonBeanCache.put(beanName, autowiringBean(beanHolder, beanConfig));
        }
//        if (singletonBeanConfigCache != null) {
//            Set<String> beanIds = singletonBeanConfigCache.keySet();
//            for (Iterator<String> it = beanIds.iterator(); it.hasNext(); ) {
//                String beanId = it.next();
//                try {
//                    //when the bean is defined as singleton, then put to the bean cache
//                    if (this.isSingleton(beanId)) {
//                        singletonBeanCache.put(beanId, beanConfig2BeanObject(singletonBeanConfigCache.get(beanId)));
//                    } else {
//                        // if not singleton, the bean should be created during access
//                        log.info("bean [{}] is not singleton!", beanId);
//                    }
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    log.error("init Bean Objects failed!", e);
//                }
//            }
//        }
    }

    private Object autowiringBean(EarlyBeanHolder parentBeanHolder, IXMLBeanConfig beanConfig) {
        boolean isAutowired = beanConfig.isAutowired();
        Object proxy = parentBeanHolder.getProxy();
        Object target = parentBeanHolder.getTarget();
        String beanName = parentBeanHolder.getBeanId();
        Class beanClass = parentBeanHolder.getBeanClass();
        boolean isAutowiring = parentBeanHolder.isAutowiring();

        Object ret = proxy;
        if (isAutowired) {
            Field[] fields = target.getClass().getDeclaredFields();
            if (fields == null || fields.length == 0) {
                return proxy;
            } else {
                for (Field field : fields) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    //if Autowired annotation exist, and
                    if (autowired != null) {
                        String depBeanName = field.getName();
                        Class depBeanType = field.getType();
                        switch (autowired.value()) {
                            case HUMMER_BEAN:
                                Object depBeanObject = singletonBeanCache.get(depBeanName);
                                //step1 if 1st cache don't have the bean
                                //step2 go to 2nd level cache, if 2nd level cache don't have the depBean
                                //
                                EarlyBeanHolder depBeanHolder = earlySingletonObjects.get(depBeanName);
                                if (depBeanHolder == null) {
                                    log.error("please add [{}.{}] to Hummer Bean XML!!!", beanClass.getSimpleName(), depBeanName);
                                    continue;
                                }
                                Assert.notNull(depBeanHolder, "dependency Bean don't exist in config:[" + depBeanName + "]");
                                parentBeanHolder.setAutowiring(true);

                                if (depBeanObject == null && !depBeanHolder.isAutowiring() && parentBeanHolder.isAutowiring()) {
                                    IXMLBeanConfig depBeanConfig = (IXMLBeanConfig) singletonBeanConfigCache.get(depBeanName);
                                    depBeanObject = autowiringBean(depBeanHolder, depBeanConfig);
                                }
//                                if (depBeanObject == null) {
//                                    parentBeanHolder.setAutowiring(true);
//                                    IXMLBeanConfig depBeanConfig = (IXMLBeanConfig) singletonBeanConfigCache.get(depBeanName);
//                                    EarlyBeanHolder depBeanHolder = earlySingletonObjects.get(depBeanName);
//                                    depBeanObject = autowiringBean(depBeanHolder, depBeanConfig);
//                                    singletonBeanCache.put(depBeanName, depBeanObject);
//                                    isAutowiring = false;
//                                } else {
//                                    isAutowiring = false;
//                                }
                                depBeanObject = depBeanHolder.getProxy();
                                if (depBeanObject != null) {
                                    //inject reference bean from cached value
                                    injectBeanProperties(target, depBeanName, depBeanObject);
//                                    singletonBeanCache.put(beanName, proxy);
                                    parentBeanHolder.setAutowiring(false);
                                    //injectBeanProperties(proxy, propertiesName, cachedPropertiesValue);
                                }
                                continue;
                            case SPRING_BEAN:
                                Object cachedSpringBeanValue = springBeanCache.get(depBeanName);
                                if (cachedSpringBeanValue == null) {
                                    cachedSpringBeanValue = HummerContainer.getInstance().getBeanFromSpring(depBeanName);
                                    springBeanCache.put(depBeanName, cachedSpringBeanValue);
                                }
                                injectBeanProperties(target, depBeanName, cachedSpringBeanValue);
                                continue;
                            case MAPPER_BEAN:
                                //TODO
                                Object cachedMapper = mapperCache.get(depBeanName);
                                if (cachedMapper == null) {
                                    cachedMapper = getMybatisMapperProxy(depBeanType);
                                    mapperCache.put(depBeanName, cachedMapper);
                                }
                                injectBeanProperties(target, depBeanName, cachedMapper);
                        }
                    }
                }
            }
            return ret;
        } else {
            Map<String, String> refBeanIds = beanConfig.getProp2RefBeanIdMapping();
            Map<String, String> propertiesValue = beanConfig.getXMLProperteisValueMapping();
            Map<String, String> springBeanIds = beanConfig.getProp2SpringBeanIdMapping();
            if (propertiesValue != null) {
                for (Iterator<String> it = propertiesValue.keySet().iterator(); it.hasNext(); ) {
                    String propertiesName = it.next();
                    String value = propertiesValue.get(propertiesName);
                    try {
                        BeanUtils.setProperty(target, propertiesName, value);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("[Manual Bean Initiation Failed!] setting properties [{}].[{}] of value [{}] failed!", target, propertiesName, value);
                        continue;
                    }
                }
            }
            // get beans from Spring container
            if (springBeanIds != null) {
                for (String propertiesName : springBeanIds.keySet()) {
                    String springBeanId = springBeanIds.get(propertiesName);
                    Object cachedSpringBeanValue = springBeanCache.get(springBeanId);
                    if (cachedSpringBeanValue == null) {
                        cachedSpringBeanValue = HummerContainer.getInstance().getBeanFromSpring(springBeanId);
                        springBeanCache.put(springBeanId, cachedSpringBeanValue);
                    }
                    injectBeanProperties(target, propertiesName, cachedSpringBeanValue);
                }
            }
            if (refBeanIds != null) {
                for (Iterator<String> it = refBeanIds.keySet().iterator(); it.hasNext(); ) {
                    String propertiesName = it.next();
                    String depBeanName = refBeanIds.get(propertiesName);
                    Object depBeanObject = singletonBeanCache.get(depBeanName);
                    if (depBeanObject == null) {
                        IXMLBeanConfig depBeanConfig = (IXMLBeanConfig) singletonBeanConfigCache.get(depBeanName);
                        EarlyBeanHolder depBeanHolder = earlySingletonObjects.get(depBeanName);
                        depBeanObject = autowiringBean(depBeanHolder, depBeanConfig);
                        singletonBeanCache.put(depBeanName, depBeanObject);
                        //inject reference bean from cached value
                        //injectBeanProperties(proxy, propertiesName, cachedPropertiesValue);
                    } else {
//                        //inject reference bean from xml config cache
//                        Object propertieValue = beanConfig2BeanObject(cachedPropertiesConfigValue);
//                        injectBeanProperties(target, propertiesName, propertieValue);
//                        //injectBeanProperties(proxy, propertiesName, propertieValue);

                    }
                    if (depBeanObject != null) {
                        injectBeanProperties(target, propertiesName, depBeanObject);
                    }
                }
            }
            return ret;
        }
    }

//    private static Object beanConfig2BeanObject(IXMLConfig beanConfig) throws
//            IllegalAccessException, InvocationTargetException {
//        Class beanClass = beanConfig.getClass();
//        EarlyBeanHolder earlyBeanHolder = earlySingletonObjects.get(beanClass);
//
//        return null;
//    }
    /*
    private Object beanConfig2BeanObject(IXMLConfig configuration)
            throws IllegalAccessException, InvocationTargetException {
        Object[] obj = null;
        Object target = null;
        Object proxy = null;
        if (configuration instanceof IXMLBeanConfig) {
            IXMLBeanConfig beanConfig = (IXMLBeanConfig) configuration;
            Class classImpl = beanConfig.getBeanClass();
            boolean isAutowired = beanConfig.isAutowired();
            //consider don't support xml config for bean initiation
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
                        IXMLConfig cachedPropertiesConfigValue =
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
                //考虑只支持Annotation注入Bean
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
                                    IXMLConfig cachedPropertiesConfig = singletonBeanConfigCache.get(beanName);
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
                                        cachedMapper = getMybatisMapperProxy(beanType);
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
    */

    /**
     * get the Mybatis bean mappers by beanType, return the MapperProxy instance
     *
     * @param beanType
     * @return
     */
    private static Object getMybatisMapperProxy(Class beanType) {
        Object cachedMapper;
        cachedMapper = MybatisUtil.getSession().getMapper(beanType);
        return cachedMapper;
    }

    /**
     * Don't need setter method to resolve the IOC of defined beans
     *
     * @param obj
     * @param propertiesName
     * @param cachedPropertiesValue
     * @throws IllegalAccessException
     */
    private static void injectBeanProperties(Object obj, String propertiesName, Object cachedPropertiesValue) {
        Class objClass = obj.getClass();
        try {
            Field tobeInjectedField = objClass.getDeclaredField(propertiesName);
            ReflectionUtil.makeAccessible(tobeInjectedField);
            tobeInjectedField.set(obj, cachedPropertiesValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("bean[{}] don't have properties[{}]to be injected by object [{}]", obj, propertiesName, cachedPropertiesValue);
        }
//        BeanUtils.setProperty(obj, propertiesName, cachedPropertiesValue);
    }

    /**
     * initiate bean by CGLIB first, return the target and proxy objects
     *
     * @param earlyBeanHolder
     * @return
     */
    private EarlyBeanHolder getProxy(EarlyBeanHolder earlyBeanHolder) {
        Object proxy = null;
        Object target = null;
        Class clazz = earlyBeanHolder.getBeanClass();
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
                earlyBeanHolder.setProxy(target);
                earlyBeanHolder.setTarget(target);
                return earlyBeanHolder;
            }
            // ret = new Object();

            Enhancer eh = new Enhancer();
            InterceptorChainCGLibCallback callback = new InterceptorChainCGLibCallback(ic, target, clazz);
            Callback[] callbacks = new Callback[]{callback};
            eh.setSuperclass(clazz);
            eh.setCallbackFilter(callback.getFilter());

            eh.setCallbacks(callbacks);
            earlyBeanHolder.setTarget(target);
            proxy = eh.create();
            earlyBeanHolder.setProxy(proxy);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            log.error("error when initial the proxy of instance: {}, pls check the default constructor!", simpleName, e);
        } catch (Throwable e) {
            log.error("error when enhance the class of instance via CGLIB: {}", simpleName, e);
            throw new RuntimeException("could not create the proxy for class :" + simpleName);
        }
        return earlyBeanHolder;
    }

//    private Object[] getProxy(Class clazz) {
//        Object[] ret = new Object[2];
//        Object target = null;
//        String simpleName = clazz.getSimpleName();
//        try {
//            if (clazz.isInterface()) {
//                String interfaceErrorMsg = "could not create the proxy for interface:" + simpleName;
//                log.error(interfaceErrorMsg);
//                throw new RuntimeException(interfaceErrorMsg);
//            }
//
//            // get the default contractor for a bean class, if don't exist or is not accessible, throw NoSuchMethodException
//            Constructor a = clazz.getConstructor();
//            target = a.newInstance();
//            // if the object is already an interceptor, return;
//            if (target instanceof Interceptor) {
//                ret[0] = target;
//                ret[1] = target;
//                return ret;
//            }
//            // ret = new Object();
//
//            Enhancer eh = new Enhancer();
//            InterceptorChainCGLibCallback callback = new InterceptorChainCGLibCallback(ic, target, clazz);
//            Callback[] callbacks = new Callback[]{callback};
//            eh.setSuperclass(clazz);
//            eh.setCallbackFilter(callback.getFilter());
//
//            eh.setCallbacks(callbacks);
//            ret[0] = target;
//            ret[1] = eh.create();
//        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
//            log.error("error when initial the proxy of instance: {}, pls check the default constructor!", simpleName, e);
//        } catch (Throwable e) {
//            log.error("error when enhance the class of instance via CGLIB: {}", simpleName, e);
//            throw new RuntimeException("could not create the proxy for class :" + simpleName);
//        }
//        return ret;
//    }
}
