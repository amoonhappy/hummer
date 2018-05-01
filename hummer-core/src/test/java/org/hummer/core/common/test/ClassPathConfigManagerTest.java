package org.hummer.core.common.test;

import junit.framework.TestCase;
import org.hummer.core.config.impl.CPConfigManager;

import java.util.Map;

public class ClassPathConfigManagerTest extends TestCase {

    public void testGetAllXMLConfiguration() {
        CPConfigManager cpcm = CPConfigManager.getInstance();
        Map a = cpcm.getAllXMLConfiguration();
        System.out.println(a.keySet());
    }

}