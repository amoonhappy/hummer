package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IXMLBeanConfig;
import org.hummer.core.config.intf.IXMLConfiguration;
import org.hummer.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class XMLBeanConfig extends XMLConfig implements IXMLBeanConfig {
    // protected List<String> enabledBeanIds;

    protected IXMLBeanConfig overwriteConfig;

    protected String beanId;

    protected Class beanClass;

    protected boolean bSingleton;

    protected String singleton;

    protected Map<String, String> refBeanIds = new HashMap<String, String>();

    protected Map<String, String> propertiesValue = new HashMap<String, String>();

    private String temp;

    public String getBeanId() {
        return this.beanId;
    }

    public void setBeanId(String serviceName) {
        this.beanId = serviceName;
    }

    public boolean isSingleton() {
        this.bSingleton = "true".equalsIgnoreCase(this.getSingleton());
        //ret;
        try {
            if (overwriteConfig != null && !StringUtil.isEmpty(overwriteConfig.getSingleton())) {
                this.bSingleton = overwriteConfig.isSingleton();
            }
        } catch (Exception e) {
            // TODO catch process
        }
        return this.bSingleton;
    }

    public IConfiguration overwriteBy(IConfiguration localConfig) {
        if (localConfig instanceof IXMLBeanConfig) {
            IXMLBeanConfig overwriteConfig = (IXMLBeanConfig) localConfig;
            this.overwriteConfig = overwriteConfig;
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
        if (this.overwriteConfig != null && overwriteConfig.getProp2RefBeanIdMapping() != null
                && overwriteConfig.getProp2RefBeanIdMapping().size() > 0) {
            ret.putAll(overwriteConfig.getProp2RefBeanIdMapping());
        }
        return ret;
    }

    public String getSingleton() {
        //String ret = this.singleton;
//        if (this.overwriteConfig != null && !StringUtil.isEmpty(overwriteConfig.getSingleton())
//                && ("true".equals(overwriteConfig.getSingleton()) || "false".equals(overwriteConfig.getSingleton()))) {
//            ret = overwriteConfig.getSingleton();
//        }
        return this.singleton;
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
