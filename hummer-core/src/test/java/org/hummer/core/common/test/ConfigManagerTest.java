package org.hummer.core.common.test;

import junit.framework.TestCase;
import org.hummer.core.config.impl.HummerConfigManager;
import org.hummer.core.config.intf.IXMLBeanConfig;
import org.hummer.core.config.intf.IXMLConfig;

public class ConfigManagerTest extends TestCase {

    public void testGetBusinessServiceConfig() {
        IXMLConfig temp = HummerConfigManager.getInstance().getXMLConfig("hummer-web-cfg-bean.xml", "pagerHandler");
        IXMLBeanConfig temp1 = (IXMLBeanConfig) temp;
        String beanId = null;
        if (temp1 != null) {
            beanId = temp1.getBeanId();
            Class implClass = temp1.getBeanClass();
            System.out.println(beanId);
            System.out.println(implClass.getCanonicalName());
        }
        assertNotNull(beanId);
    }
}