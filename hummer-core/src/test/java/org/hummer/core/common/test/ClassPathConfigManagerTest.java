package org.hummer.core.common.test;

import junit.framework.TestCase;
import org.hummer.core.config.impl.HummerConfigManager;

import java.util.Map;

public class ClassPathConfigManagerTest extends TestCase {

    public void testGetAllXMLConfiguration() {
        HummerConfigManager cpcm = HummerConfigManager.getInstance();
        Map a = cpcm.getAllXMLConfiguration();
        System.out.println(a.keySet());
    }

}