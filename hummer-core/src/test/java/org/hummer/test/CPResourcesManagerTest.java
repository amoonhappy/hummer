package org.hummer.test;

import junit.framework.TestCase;
import org.hummer.core.config.impl.CPResourcesManager;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CPResourcesManagerTest extends TestCase {
    private static Logger log = Log4jUtils.getLogger(CPResourcesManagerTest.class);
    private String file = "hummer-cfg-main.properties";
    private String file1 = "hummer-web-cfg-aop.xml";

    public void testGetCore() {

        try {
            InputStream input = CPResourcesManager.getInstance().getCore("hummer-core.id");
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("core:" + line);
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {

            InputStream input = CPResourcesManager.getInstance().getLocal(file1);
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("local:" + line);
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
