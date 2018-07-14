package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IXMLBeanConfig;
import org.hummer.core.config.intf.IXMLConfiguration;
import org.hummer.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class XMLBeanConfig extends XMLConfig implements IXMLBeanConfig {
    // protected List<String> enabledBeanIds;

    IXMLBeanConfig overwriteConfig;

    private String beanId;

    private Class beanClass;

    private String singleton;

    private String autowired;

    private Map<String, String> refBeanIds = new HashMap<>();

    private Map<String, String> propertiesValue = new HashMap<>();

    private Map<String, String> springBeanIds = new HashMap<>();

    public String getBeanId() {
        return this.beanId;
    }

    public void setBeanId(String serviceName) {
        this.beanId = serviceName;
    }

    public boolean isSingleton() {
        boolean bSingleton = "true".equalsIgnoreCase(this.getSingleton());

        if (overwriteConfig != null && !StringUtil.isEmpty(overwriteConfig.getSingleton())) {
            bSingleton = overwriteConfig.isSingleton();
        }

        return bSingleton;
    }

    public boolean isAutowired() {
        boolean b = "true".equalsIgnoreCase(this.getAutowired());

        if (overwriteConfig != null && !StringUtil.isEmpty(overwriteConfig.getAutowired())) {
            b = overwriteConfig.isAutowired();
        }
        return b;
    }

    public IConfiguration overwriteBy(IConfiguration localConfig) {
        if (localConfig instanceof IXMLBeanConfig) {
            this.overwriteConfig = (IXMLBeanConfig) localConfig;
        }
        return this;
    }

    public Class getBeanClass() {
        if (overwriteConfig != null && overwriteConfig.getBeanClass() != null) {
            return overwriteConfig.getBeanClass();
        } else {
            return this.beanClass;
        }
    }

    public void setBeanClass(Class implClass) {
        this.beanClass = implClass;
    }

    public void regRefBeanId(String key, String refBeanId) {
        if (!StringUtil.isEmpty(refBeanId) && !StringUtil.isEmpty(key)) {
            getProp2RefBeanIdMapping().put(key, refBeanId);
        }
    }

    public Map<String, String> getProp2RefBeanIdMapping() {
        Map<String, String> ret = this.refBeanIds;
        if (this.overwriteConfig != null) {
            Map<String, String> prop2RefBeanMap = overwriteConfig.getProp2RefBeanIdMapping();
            if (prop2RefBeanMap != null && prop2RefBeanMap.size() > 0) {
                ret.putAll(prop2RefBeanMap);
            }
        }
        return ret;
    }

    public void regSpringBeanId(String key, String springBeanId) {
        if (!StringUtil.isEmpty(springBeanId) && !StringUtil.isEmpty(key)) {
            getProp2SpringBeanIdMapping().put(key, springBeanId);
        }
    }

    public Map<String, String> getProp2SpringBeanIdMapping() {
        Map<String, String> ret = this.springBeanIds;
        if (this.overwriteConfig != null) {
            Map<String, String> prop2SpringBeanMap = overwriteConfig.getProp2SpringBeanIdMapping();
            if (prop2SpringBeanMap != null && prop2SpringBeanMap.size() > 0) {
                ret.putAll(prop2SpringBeanMap);
            }
        }
        return ret;
    }

    public String getAutowired() {
        return this.autowired;
    }

    public String getSingleton() {
        //String ret = this.singleton;
//        if (this.overwriteConfig != null && !StringUtil.isEmpty(overwriteConfig.getSingleton())
//                && ("true".equals(overwriteConfig.getSingleton()) || "false".equals(overwriteConfig.getSingleton()))) {
//            ret = overwriteConfig.getSingleton();
//        }
        return this.singleton;
    }

    public void setAutowired(String autowired) {
        this.autowired = autowired;
    }

    public void setSingleton(String singletonFlag) {
        this.singleton = singletonFlag;
    }

    public Map<String, String> getXMLProperteisValueMapping() {
        if (overwriteConfig != null && overwriteConfig.getXMLProperteisValueMapping() != null
                && overwriteConfig.getXMLProperteisValueMapping().size() > 0) {
            propertiesValue.putAll(overwriteConfig.getXMLProperteisValueMapping());
        }
        return propertiesValue;
    }

    public void regPropertiesValue(String propertiesName, String value) {
        this.propertiesValue.put(propertiesName, value);
    }

    public Map<String, IXMLConfiguration> getChildMap() {
        if (overwriteConfig != null && overwriteConfig.getChildMap() != null
                && overwriteConfig.getChildMap().size() > 0) {
            configCache.putAll(overwriteConfig.getChildMap());
        }
        return configCache;
    }
    //
    // @Override
    // public List<String> getEnabledBeanIds() {
    // return enabledBeanIds;
    // }
    //
    // @Override
    // public void regEnabledBeanIds(String propertyName, String value) {
    //
    // }
}
