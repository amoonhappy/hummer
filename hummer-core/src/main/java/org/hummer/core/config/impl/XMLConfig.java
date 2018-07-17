package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IXMLConfig;

import java.util.HashMap;
import java.util.Map;

public abstract class XMLConfig implements IXMLConfig {
    // protected IXMLConfig overwriteConfig;

    Map<String, IXMLConfig> configCache = new HashMap<>();

    public Object getValue(String key) {
        return getChildMap().get(key);
    }

    public void setChild(Map<String, IXMLConfig> child) {
        configCache = child;
    }

    // public Map<String, IXMLConfig> getChildMap() {
    // if (overwriteConfig != null && overwriteConfig.getChildMap() != null
    // && overwriteConfig.getChildMap().size() > 0) {
    // configCache.putAll(overwriteConfig.getChildMap());
    // }
    // return configCache;
    // }
}
