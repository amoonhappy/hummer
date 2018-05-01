package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IPropConfigParser;
import org.hummer.core.config.intf.IXMLConfiguration;
import org.hummer.core.util.FileSystemUtil;

import java.io.FileNotFoundException;
import java.util.Map;

public class ConfigFactory implements IPropConfigParser {
    private static ConfigFactory instance = new ConfigFactory();

    public static ConfigFactory getInstance() {
        return instance;
    }

    public IConfiguration parse(String fileName) throws FileNotFoundException {
        String extentionName = FileSystemUtil.getFileExtention(fileName);
        IConfiguration ret = null;
        if (ParserFactory.PROP_FILEEXT.equals(extentionName)) {
            IPropConfigParser parser = new PropConfigParser();
            ret = parser.parse(fileName);
        } else if (ParserFactory.XML_FILEEXT.equals(extentionName)) {
            XMLConfigFactory xmlConfigFactory = XMLConfigFactory.getInstance();
            Map<String, IXMLConfiguration> map = xmlConfigFactory.parse(fileName);
            IXMLConfiguration xml = new XMLBeanConfig();
            xml.setChild(map);
            return xml;
        }
        return ret;
    }
}
