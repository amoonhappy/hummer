package org.hummer.core.common.test;

import junit.framework.TestCase;
import org.hummer.core.config.impl.PropConfigParser;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IPropConfigParser;

import java.io.FileNotFoundException;

public class PropConfigParserTest extends TestCase {

    public PropConfigParserTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParse() {
        String fileName = "hummer-cfg-test.properties";
        IPropConfigParser parser = new PropConfigParser();
        try {
            IConfiguration config = parser.parse(fileName);
            String value = (String) config.getValue("testKey");
            assertEquals(value, "localTestValue");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
