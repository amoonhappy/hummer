package org.hummer.core.config.impl;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.hummer.core.config.intf.IXMLBusinessServiceConfig;
import org.hummer.core.config.intf.IXMLConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceParser {

    public static Map<String, IXMLConfiguration> parse(Element xmlDoc) {
        Map<String, IXMLConfiguration> ret = new HashMap<String, IXMLConfiguration>();

        List<Element> beans = xmlDoc.elements();
        for (int i = 0; i < beans.size(); i++) {
            Element bsv = beans.get(i);
            String className = StringUtils.trimToEmpty(bsv.attributeValue("class"));
            String serviceName = bsv.attributeValue("id");
            String singletonFlag = bsv.attributeValue("singleton");
            String tansactionType = bsv.attributeValue("transType");
            try {
                Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
                // Class clazz =
                // ServiceParser.class.getClassLoader().loadClass(className);
                // Class clazz = null;
                IXMLBusinessServiceConfig tempBsvConfig = new XMLBizServiceConfig();

                tempBsvConfig.setBeanClass(clazz);
                tempBsvConfig.setBeanId(serviceName);
                tempBsvConfig.setSingleton(singletonFlag);
                tempBsvConfig.setTransactionType(tansactionType);
                ret.put(serviceName, tempBsvConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
