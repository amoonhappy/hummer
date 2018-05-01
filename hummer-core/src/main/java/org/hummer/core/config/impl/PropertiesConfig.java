package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.util.StringUtil;

import java.util.Properties;

public class PropertiesConfig implements IConfiguration {
    Properties props;

    IConfiguration overwriteConfig;

    // Map<String, Object> props = newweb HashMap<String, Object>();

    public Object getValue(String key) {
        Object ret = null;
        if (props != null) {
            ret = props.get(key);
            if (overwriteConfig != null) {
                ret = overwriteConfig.getValue(key);
                if (StringUtil.isEmpty((String) ret)) {
                    ret = props.get(key);
                }
            }
        }
        return ret;
    }

    public IConfiguration overwriteBy(IConfiguration localConfig) {
        this.overwriteConfig = localConfig;
        return this;
    }

    public void setProperties(Properties props) {
        this.props = props;
    }

}
