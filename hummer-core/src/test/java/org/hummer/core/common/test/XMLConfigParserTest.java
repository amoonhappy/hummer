package org.hummer.core.common.test;

import junit.framework.TestCase;
import org.hummer.core.config.impl.XMLConfigFactory;
import org.hummer.core.config.intf.IXMLConfiguration;

import java.util.Map;

public class XMLConfigParserTest extends TestCase {

    public void testParse() {
        String fileName = "hummer-web-cfg-service.xml";
        XMLConfigFactory factory = XMLConfigFactory.getInstance();
        Map<String, IXMLConfiguration> ret = factory.parse(fileName);
        IXMLConfiguration config = ret.get("userService");
        System.out.println(config);
    }
}