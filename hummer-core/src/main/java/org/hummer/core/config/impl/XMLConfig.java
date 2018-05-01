package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IXMLConfiguration;

import java.util.HashMap;
import java.util.Map;

public abstract class XMLConfig implements IXMLConfiguration {
    // protected IXMLConfiguration overwriteConfig;

    protected Map<String, IXMLConfiguration> configCache = new HashMap<String, IXMLConfiguration>();

    public Object getValue(String key) {
        return getChildMap().get(key);
    }

    public void setChild(Map<String, IXMLConfiguration> child) {
        configCache = child;
    }

    // public Map<String, IXMLConfiguration> getChildMap() {
    // if (overwriteConfig != null && overwriteConfig.getChildMap() != null
    // && overwriteConfig.getChildMap().size() > 0) {
    // configCache.putAll(overwriteConfig.getChildMap());
    // }
    // return configCache;
    // }
}
