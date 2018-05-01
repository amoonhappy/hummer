package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IPropConfigParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropConfigParser implements IPropConfigParser {

    private static Properties props;

    public IConfiguration parse(String fileName) throws FileNotFoundException {
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
        InputStream in;
        props = new Properties();
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(localFileName);
            if (in != null) {
                props.load(in);
                ret.setProperties(props);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }

    private IConfiguration loadCoreConfig(String fileName) {
        String coreFileName = "core/config/" + fileName;
        PropertiesConfig ret = new PropertiesConfig();
        InputStream in;
        props = new Properties();
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(coreFileName);
            if (in != null) {
                props.load(in);
                ret.setProperties(props);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String parseConfigType(String fileName) throws FileNotFoundException {
        return CONFIG_TYPE_ALL;
    }

}
