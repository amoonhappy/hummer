package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IPropConfigParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropConfigParser implements IPropConfigParser {

    private static Properties props;

    public IConfiguration parse(String fileName) {
        // load core config
        IConfiguration coreConfig = loadCoreConfig(fileName);
        // load local config
        IConfiguration localConfig = loadLocalConfig(fileName);

        coreConfig = coreConfig.overwriteBy(localConfig);

        return coreConfig;
    }

    private IConfiguration loadLocalConfig(String fileName) {
        String localFileName = "local/config/" + fileName;
        PropertiesConfig ret = new PropertiesConfig();
        props = new Properties();
        ret = loadStream2Prop(localFileName, ret);
        return ret;
    }

    private PropertiesConfig loadStream2Prop(String localFileName, PropertiesConfig ret) {
        InputStream in;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(localFileName);
            if (in != null) {
                props.load(in);
                ret.setProperties(props);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private IConfiguration loadCoreConfig(String fileName) {
        String coreFileName = "core/config/" + fileName;
        PropertiesConfig ret = new PropertiesConfig();
        props = new Properties();
        ret = loadStream2Prop(coreFileName, ret);

        return ret;
    }
}
