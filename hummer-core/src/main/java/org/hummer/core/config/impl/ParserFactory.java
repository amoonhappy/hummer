package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IPropertiesParser;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IXMLConfig;
import org.hummer.core.util.FileSystemUtil;

import java.io.FileNotFoundException;
import java.util.Map;

public class ParserFactory implements IPropertiesParser {
    private static final String PROP_FILE_EXT = "properties";
    private static final String XML_FILE_EXT = "xml";
    private static ParserFactory instance = new ParserFactory();

    public static ParserFactory getInstance() {
        return instance;
    }

    public IConfiguration parse(String fileName) throws FileNotFoundException {
        String extensionName = FileSystemUtil.getFileExtention(fileName);
        IConfiguration ret = null;
        if (PROP_FILE_EXT.equals(extensionName)) {
            IPropertiesParser parser;
            parser = new PropertiesParser();
            ret = parser.parse(fileName);
        } else if (XML_FILE_EXT.equals(extensionName)) {
            XMLConfigFactory xmlConfigFactory = XMLConfigFactory.getInstance();
            Map<String, IXMLConfig> map = xmlConfigFactory.parse(fileName);
            IXMLConfig xml = new XMLBeanConfig();
            xml.setChild(map);
            return xml;
        }
        return ret;
    }
}
