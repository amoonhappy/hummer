package org.hummer.core.config.impl;

import org.hummer.core.config.intf.ICPPropConfigParser;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IXMLConfiguration;
import org.hummer.core.util.FileSystemUtil;

import java.io.FileNotFoundException;
import java.util.Map;

public class CPConfigFactory implements ICPPropConfigParser {
    private static CPConfigFactory instance = new CPConfigFactory();

    public static CPConfigFactory getInstance() {
        return instance;
    }

    public IConfiguration parse(String fileName) throws FileNotFoundException {
        String extensionName = FileSystemUtil.getFileExtention(fileName);
        IConfiguration ret = null;
        if (ParserFactory.PROP_FILE_EXT.equals(extensionName)) {
            ICPPropConfigParser parser;
            parser = new CPPropConfigParser();
            ret = parser.parse(fileName);
        } else if (ParserFactory.XML_FILE_EXT.equals(extensionName)) {
            XMLConfigFactory xmlConfigFactory = XMLConfigFactory.getInstance();
            Map<String, IXMLConfiguration> map = xmlConfigFactory.parse(fileName);
            IXMLConfiguration xml = new XMLBeanConfig();
            xml.setChild(map);
            return xml;
        }
        return ret;
    }
}
