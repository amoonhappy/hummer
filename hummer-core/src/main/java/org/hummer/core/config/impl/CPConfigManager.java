package org.hummer.core.config.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hummer.core.config.intf.IConfigManager;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IPropConfigParser;
import org.hummer.core.config.intf.IXMLConfiguration;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CPConfigManager implements IConfigManager {
    private static Logger log = Log4jUtils.getLogger(CPConfigManager.class);
    private static CPConfigManager instance = new CPConfigManager();
    private IConfiguration archConfig;
    private Map<String, IConfiguration> configCache = new HashMap<>();

    private CPConfigManager() {

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

    public static CPConfigManager getInstance() {
        return instance;
    }

    public Map<String, IXMLConfiguration> getAllXMLConfiguration() {
        Map<String, IXMLConfiguration> ret = new HashMap<>();
        if (configCache != null && configCache.size() > 0) {
            Set<String> keySet = configCache.keySet();
            for (String key : keySet) {
                IConfiguration config = configCache.get(key);
                if (config instanceof IXMLConfiguration) {
                    IXMLConfiguration xmlConfig = (IXMLConfiguration) config;
                    Map<String, IXMLConfiguration> xmlConfigMapping = xmlConfig.getChildMap();
                    ret.putAll(xmlConfigMapping);
                }
            }
        }
        return ret;
    }

    public IXMLConfiguration getXMLConfig(String fileName, String serviceName) {
        IConfiguration temp = configCache.get(fileName);
        IXMLConfiguration ret = null;
        if (temp instanceof IXMLConfiguration) {
            IXMLConfiguration serviceConfig = (IXMLConfiguration) temp;
            ret = (IXMLConfiguration) serviceConfig.getValue(serviceName);
        }

        return ret;
    }

    private void initialComponentConfig() throws IOException {
        Set<SupportedAppInfos.SupportedAppInfo> apps = SupportedAppInfos.getRegAppInfos();
        Set<String> nameList = new HashSet<>();

        SupportedAppInfos.SupportedAppInfo appInfo;
        for (SupportedAppInfos.SupportedAppInfo app : apps) {
            String comp_id;
            appInfo = app;
            comp_id = appInfo.getAppName();
            log.info(comp_id);
            nameList = findCompConfig(nameList, comp_id);

            for (String fileName : nameList) {
                log.info(comp_id + "." + fileName);
                configCache.put(fileName, CPConfigFactory.getInstance().parse(fileName));
            }
        }
    }

    //    private void findCompConfig(Set<String> nameList, String comp_id) {
//        JarFile jar;
//        Enumeration<JarEntry> jarEntries;
//        jar = CPResourcesManager.getInstance().getJar(comp_id);
//        jarEntries = jar == null ? null : jar.entries();
//        while (jarEntries != null && jarEntries.hasMoreElements()) {
//            String fileName;
//            JarEntry je = jarEntries.nextElement();
//            fileName = je.getName();
//            if ((StringUtils.contains(je.getName(), CPResourcesManager.CORE_PREFIX) || StringUtils.contains(je
//                    .getName(), CPResourcesManager.LOCAL_PREFIX))
//                    && (je.getName().endsWith(".properties") || je.getName().endsWith(".xml"))) {
//                fileName = StringUtils.replace(fileName, CPResourcesManager.CORE_PREFIX, "");
//                fileName = StringUtils.replace(fileName, CPResourcesManager.LOCAL_PREFIX, "");
//                nameList.add(fileName);
//            }
//        }
//    }
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
        URL url = this.getClass().getClassLoader().getResource(CPResourcesManager.CORE_PREFIX + s);
        log.info("checkConfigExistorNot: " + s);
        log.info("checkConfigExistorNot: " + url);

        if (url != null) {
            nameList.add(s);
        } else {
            URL urlaop_local = this.getClass().getClassLoader().getResource(CPResourcesManager.LOCAL_PREFIX + s);
            if (urlaop_local != null)
                nameList.add(s);
        }
        return nameList;
    }

    private void initialArchConfig() throws FileNotFoundException {
        IPropConfigParser parser = new PropConfigParser();

        archConfig = parser.parse(archConfigFileName);
    }

    private void initSupportedApp() {
        if (archConfig != null) {
            String comp_ids = (String) archConfig.getValue(HUMMER_COMPONENT_ID);
            if (!StringUtil.isEmpty(comp_ids)) {
                String[] comp_ids_ar = StringUtil.joinArray(comp_ids, ",");
                String comp_ver_key;
                String comp_ver;
                for (String comp_id : comp_ids_ar) {
                    comp_ver_key = StringUtils.replace(HUMMER_COMPONENT_VER, HUMMER_COMPONENT_REPLACEMENT, comp_id);
                    comp_ver = (String) archConfig.getValue(comp_ver_key);
                    SupportedAppInfos.appReg(comp_id, comp_ver);
                    if (log.isInfoEnabled()) {
                        log.info("Supported Component:[" + comp_id + "] ver:[" + comp_ver + "]");
                    }
                }
            }
        }
    }
}
