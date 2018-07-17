package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IPropertiesParser;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesParser implements IPropertiesParser {
    private static Logger log = Log4jUtils.getLogger(PropertiesParser.class);

    public IConfiguration parse(String fileName) {
        // load core config
        IConfiguration coreConfig = loadCoreConfig(fileName);
        // load local config
        IConfiguration localConfig = loadLocalConfig(fileName);

        coreConfig = coreConfig.overwriteBy(localConfig);

        return coreConfig;
    }

    private IConfiguration loadLocalConfig(String fileName) {
        String localFileName = HummerConfigManager.LOCAL_CONFIG_PATH_PREFIX + fileName;
        PropertiesConfig ret = new PropertiesConfig();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        ret = loadStream2Prop(localFileName, ret, cl);

        return ret;
    }

    private IConfiguration loadCoreConfig(String fileName) {
        String coreFileName = HummerConfigManager.CORE_CONFIG_PATH_PREFIX + fileName;
        PropertiesConfig ret = new PropertiesConfig();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        loadStream2Prop(coreFileName, ret, cl);

        return ret;
    }

    private PropertiesConfig loadStream2Prop(String coreFileName, PropertiesConfig ret, ClassLoader cl) {
        InputStream is;
        try {
            is = cl.getResourceAsStream(coreFileName);
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                ret.setProperties(props);
            }
        } catch (IOException e) {
            log.error("could not load [" + coreFileName + "] from " + cl, e);
        }
        return ret;
    }
}
