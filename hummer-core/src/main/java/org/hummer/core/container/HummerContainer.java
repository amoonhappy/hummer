package org.hummer.core.container;

import org.hummer.core.config.impl.HummerConfigManager;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.factory.impl.HummerBeanFactory;
import org.hummer.core.util.DateUtil;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HummerContainer {
    public static final String DS_POOL_DRUID = "druid";
    public static final String DS_POOL_HIKARI = "hikari";
    private static final Logger log = Log4jUtils.getLogger(HummerContainer.class);
    private static HummerContainer instance = new HummerContainer();
    private ApplicationContext ctx = null;
    private Date initTime;
    private HummerConfigManager configManager;
    //Default DS is druid
    private String dsType = DS_POOL_DRUID;
    private List<String> postAutowireSpringBeanNameList = new ArrayList<>();
    private ConcurrentHashMap<String, Object> postAutowireSpringBeanMap = new ConcurrentHashMap<>();
    private List<String> postAutowireSpringMVCBeanNameList = new ArrayList<>();
    private ConcurrentHashMap<String, Object> postAutowireSpringMVCBeanMap = new ConcurrentHashMap<>();

    private HummerContainer() {
        init();
        postInit();
        initTime = new Date();
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

    public void postAutowireSpringBeans() {
        HummerBeanFactory.getInstance().addPostAutowireSpringMVCBeanNameList(this.postAutowireSpringMVCBeanNameList);
        HummerBeanFactory.getInstance().addPostAutowireSpringMVCBeanMap(this.postAutowireSpringMVCBeanMap);
        HummerBeanFactory.getInstance().addPostAutowireSpringBeanNameList(this.postAutowireSpringBeanNameList);
        HummerBeanFactory.getInstance().addPostAutowireSpringBeanMap(this.postAutowireSpringBeanMap);

        HummerBeanFactory.getInstance().postSpringMVCBeanAutowire();
        HummerBeanFactory.getInstance().postSpringBeanAutowire();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Hummer Container: start up date [");
        stringBuilder.append(DateUtil.format(initTime, DateUtil.SDF_CN));
        stringBuilder.append("];");
        stringBuilder.append(" HashCode is [@");
        stringBuilder.append(Integer.toHexString(hashCode()));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /**
     * initialization of Hummer's Component Config Manager
     */
    private void init() {
        configManager = HummerConfigManager.getInstance();
        IConfiguration archConfig = configManager.getHummerMainCfg();
        if (archConfig != null) {
            String db_conn_pool_type = (String) archConfig.getValue(HummerConfigManager.HUMMER_DB_CONN_POOL_TYPE);
            if (db_conn_pool_type != null) {
                log.info("DB connection pool type is: [{}]", db_conn_pool_type);
            } else {
                db_conn_pool_type = HummerContainer.DS_POOL_DRUID;
                log.info("No DB connection pool type found in Hummer's main config! using default type: [{}]", db_conn_pool_type);
            }
            this.dsType = db_conn_pool_type;
        }
        //read ds config
    }

    /**
     * initialization of Hummer's Bean Factory Config Manager
     */
    private void postInit() {
//        beanFactory = HummerBeanFactory.getInstance();
    }

    public static HummerContainer getInstance() {
        return instance;
    }

    public <T> T getBean(String beanName, final Class<T> requiredType) {
        return (T) HummerBeanFactory.getInstance().getBean(beanName);
    }

    public HummerConfigManager getConfigManager() {
        return configManager;
    }

    public HummerBeanFactory getBeanFactory() {
        return HummerBeanFactory.getInstance();
    }

    public ApplicationContext getSpringContext() {
        return ctx;
    }

    public void reInit() {
        configManager.reInit();
        HummerBeanFactory.getInstance().reInit();
        init();
    }


    public void bondWithSpringContext(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public Object getBeanFromSpring(String beanName) {
        if (StringUtil.isEmpty(beanName) || this.ctx == null) {
            return null;
        } else {
            return this.ctx.getBean(beanName);
        }
    }

    public String getDataSourcePoolType() {
        return dsType;
    }
}
