package org.hummer.core.config.impl;

import org.apache.log4j.Logger;
import org.hummer.core.config.intf.ICPPropConfigParser;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.util.Log4jUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CPPropConfigParser implements ICPPropConfigParser {
    private static Logger log = Log4jUtils.getLogger(CPPropConfigParser.class);

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
        InputStream is;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            is = cl.getResourceAsStream(localFileName);
            if (is != null) {
                props = new Properties();
                props.load(is);
                ret.setProperties(props);
            }
        } catch (FileNotFoundException e) {
            log.error("could not load [" + localFileName + "] from " + cl, e);
        } catch (IOException e) {
            log.error("could not load [" + localFileName + "] from " + cl, e);
        }

        return ret;
    }

    private IConfiguration loadCoreConfig(String fileName) {
        String coreFileName = "core/config/" + fileName;
        PropertiesConfig ret = new PropertiesConfig();
        InputStream is;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {

            is = cl.getResourceAsStream(coreFileName);
            if (is != null) {
                props = new Properties();
                props.load(is);
                ret.setProperties(props);
            }
        } catch (FileNotFoundException e) {
            log.error("could not load [" + coreFileName + "] from " + cl, e);
        } catch (IOException e) {
            log.error("could not load [" + coreFileName + "] from " + cl, e);
        }

        return ret;
    }

    public String parseConfigType(String fileName) throws FileNotFoundException {
        return CONFIG_TYPE_ALL;
    }

}
