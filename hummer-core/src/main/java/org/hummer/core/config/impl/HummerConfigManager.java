package org.hummer.core.config.impl;

import org.apache.commons.lang3.StringUtils;
import org.hummer.core.cache.impl.CacheManager;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IPropertiesParser;
import org.hummer.core.config.intf.IXMLConfig;
import org.hummer.core.util.DateUtil;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HummerConfigManager {
    public static final String HUMMER_DB_CONN_POOL_TYPE = "hummer.db_conn_pool_type";

    private static final String HUMMER_CACHE_EXPIRATION_ENABLED = "hummer.cache.expiration.enabled";
    private static final String HUMMER_CACHE_EXPIRATION_TIME = "hummer.cache.expiration.time";
    private static final String HUMMER_COMPONENT_ID = "hummer.component_id";
    private static final String HUMMER_COMPONENT_VER = "hummer.component_id.version";
    private static final String HUMMER_COMPONENT_REPLACEMENT = "component_id";
    private static final String HUMMER_CFG_MAIN_PROPERTIES = "hummer-cfg-main.properties";
    public static final String JOINER = "-";
    public static final String CONFIG = "cfg";
    public static final String CORE_CONFIG_PATH_PREFIX = "core/config/";
    public static final String LOCAL_CONFIG_PATH_PREFIX = "local/config/";
    public static final String PATH_SEPERATOR = "/";
    private static Logger log = Log4jUtils.getLogger(HummerConfigManager.class);
    private static HummerConfigManager instance = new HummerConfigManager();
    private static IConfiguration archConfig;
    private Map<String, IConfiguration> configCache = new HashMap<>();
    private Date initTime;

    private HummerConfigManager() {
        init();
        postInit();
        initTime = new Date();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Hummer Comp Config Manager: start up date [");
        stringBuilder.append(DateUtil.format(initTime, DateUtil.SDF_CN));
        stringBuilder.append("];");
        stringBuilder.append(" HashCode is [@");
        stringBuilder.append(Integer.toHexString(hashCode()));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private void postInit() {
        initCacheConfig();
    }

    private void initCacheConfig() {
        boolean expEnabled = Boolean.valueOf((String) archConfig.getValue(HUMMER_CACHE_EXPIRATION_ENABLED));
        int expPeriod = Integer.valueOf((String) archConfig.getValue(HUMMER_CACHE_EXPIRATION_TIME));

        CacheManager.setExpirationEnabled(expEnabled);
        CacheManager.setExpirationPeriod(expPeriod);
    }

    public static HummerConfigManager getInstance() {
        return instance;
    }

    public Map<String, IXMLConfig> getAllXMLConfiguration() {
        Map<String, IXMLConfig> ret = new HashMap<>();
        if (configCache != null && configCache.size() > 0) {
            Set<String> keySet = configCache.keySet();
            for (String key : keySet) {
                IConfiguration config = configCache.get(key);
                if (config instanceof IXMLConfig) {
                    IXMLConfig xmlConfig = (IXMLConfig) config;
                    Map<String, IXMLConfig> xmlConfigMapping = xmlConfig.getChildMap();
                    ret.putAll(xmlConfigMapping);
                }
            }
        }
        return ret;
    }

    public IXMLConfig getXMLConfig(String fileName, String serviceName) {
        IConfiguration temp = configCache.get(fileName);
        IXMLConfig ret = null;
        if (temp instanceof IXMLConfig) {
            IXMLConfig serviceConfig = (IXMLConfig) temp;
            ret = (IXMLConfig) serviceConfig.getValue(serviceName);
        }

        return ret;
    }

    private void initialComponentConfig() throws IOException {
        Set<SupportedComponent.SupportedComponentInfo> apps = SupportedComponent.getSupportedComponentInfos();
        Set<String> nameList = new HashSet<>();
        String comp_id;

        SupportedComponent.SupportedComponentInfo appInfo;
        for (Iterator<SupportedComponent.SupportedComponentInfo> it = apps.iterator(); it.hasNext(); ) {
            appInfo = it.next();
            comp_id = appInfo.getCompName();
            if (comp_id != null) {
                log.debug("Initialization Component:[{}]", comp_id);
                nameList = findCompConfig(nameList, comp_id);
                if (nameList != null && nameList.size() > 0) {
                    Iterator<String> itName = nameList.iterator();
                    while (itName.hasNext()) {
                        String fileName = itName.next();
                        log.debug("Caching [{}]", fileName);
                        configCache.put(fileName, ParserFactory.getInstance().parse(fileName));
                    }
                }
            }
        }
    }

    private Set<String> findCompConfig(Set<String> nameList, String comp_id) {
        String aop_config_file_name = comp_id + "-cfg-aop.xml";
        String bean_config_file_name = comp_id + "-cfg-bean.xml";
        String dao_config_file_name = comp_id + "-cfg-dao.xml";
        String service_config_file_name = comp_id + "-cfg-service.xml";

        nameList = checkConfigExistorNot(nameList, aop_config_file_name);
        nameList = checkConfigExistorNot(nameList, bean_config_file_name);
        nameList = checkConfigExistorNot(nameList, dao_config_file_name);
        nameList = checkConfigExistorNot(nameList, service_config_file_name);
        return nameList;
    }

    private Set<String> checkConfigExistorNot(Set<String> nameList, String s) {
        URL url = this.getClass().getClassLoader().getResource(CORE_CONFIG_PATH_PREFIX + s);
        log.debug("checkConfigExistorNot: {}", s);
        log.debug("checkConfigExistorNot: {}", url);

        if (nameList == null) {
            nameList = new HashSet<>();
        }

        if (url != null) {
            nameList.add(s);
        } else {
            URL urlaop_local = this.getClass().getClassLoader().getResource(LOCAL_CONFIG_PATH_PREFIX + s);
            if (urlaop_local != null) {
                nameList.add(s);
            } else {
//                //if the configuration files of defined comp_id are not found, return null
//                return null;
            }
        }
        return nameList;
    }

    public IConfiguration getArchConfig() {
        return archConfig;
    }

    private void initialArchConfig() throws FileNotFoundException {
        IPropertiesParser parser = new PropertiesParser();

        archConfig = parser.parse(HUMMER_CFG_MAIN_PROPERTIES);
    }

    public IConfiguration getHummerMainCfg() {
        return archConfig;
    }

    public Set<SupportedComponent.SupportedComponentInfo> getSupportedComponents() {
        Set<SupportedComponent.SupportedComponentInfo> supported = SupportedComponent.getSupportedComponentInfos();
        return supported;
    }

    private void init() {
        try {
            initialArchConfig();
        } catch (FileNotFoundException e) {
            log.error("Arch Config not found", e);
        }
        initSupportedApp();
        try {
            initialComponentConfig();
        } catch (IOException e) {
            log.error("Comp Config not found", e);
        }
    }

    public void reInit() {
//        archConfig = null;
//
//        configCache = new HashMap<>();
        instance = new HummerConfigManager();
    }

    private void initSupportedApp() {
        if (archConfig != null) {
            String comp_ids = (String) archConfig.getValue(HUMMER_COMPONENT_ID);
            if (!StringUtil.isEmpty(comp_ids)) {
                String[] comp_ids_ar = StringUtil.joinArray(comp_ids, ",");
                String comp_ver_key;
                String comp_ver;
                for (int i = 0; i < comp_ids_ar.length; i++) {
                    String comp_id = comp_ids_ar[i];
                    comp_ver_key = StringUtils.replace(HUMMER_COMPONENT_VER, HUMMER_COMPONENT_REPLACEMENT, comp_id);
                    comp_ver = (String) archConfig.getValue(comp_ver_key);
                    SupportedComponent.regComponent(comp_id, comp_ver);
                    log.info("Supported Component:[{}], Version:[{}]", comp_id, comp_ver);
                }
            }
        }
    }
}
