package org.hummer.core.config.impl;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.hummer.core.config.intf.IXMLBeanConfig;
import org.hummer.core.config.intf.IXMLConfigParser;
import org.hummer.core.config.intf.IXMLConfiguration;
import org.hummer.core.util.ClassLoaderUtil;
import org.hummer.core.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLBeanConfigParser implements IXMLConfigParser {

    public Map<String, IXMLConfiguration> parse(Element xmlDoc) {
        Map<String, IXMLConfiguration> ret = new HashMap<String, IXMLConfiguration>();

        List<Element> beans = xmlDoc.elements();
        for (int i = 0; i < beans.size(); i++) {
            Element bean = beans.get(i);
            IXMLBeanConfig xmlBeanConfig = parseBeanConfig(bean);
            ret.put(xmlBeanConfig.getBeanId(), xmlBeanConfig);
        }
        return ret;
    }

    protected String parseAttribute(Element e, String attrName) {
        String value = null;
        if (e != null && !StringUtil.isEmpty(attrName)) {
            value = StringUtils.trimToEmpty(e.attributeValue(attrName));
        }
        return value;
    }

    protected IXMLBeanConfig parseBeanConfig(Element e) {
        IXMLBeanConfig ret = getXMLBeanConfigImpl();
        ret = parseBeanClassValue(e, ret);
        ret = parseBeanIdValue(e, ret);
        ret = parseSingletonValue(e, ret);
        ret = parsePropertiesValue(e, ret);
        ret = parseOtherAttribute(e, ret);
        return ret;
    }

    protected IXMLBeanConfig parsePropertiesValue(Element e, IXMLBeanConfig ret) {
        if (e != null && ret != null) {
            List<Element> properties = e.elements("properties");
            for (int i = 0; i < properties.size(); i++) {
                Element property = properties.get(i);
                ret = parseBeanAttrConfig(property, ret);
            }
        }
        return ret;
    }

    protected IXMLBeanConfig parseBeanAttrConfig(Element e, IXMLBeanConfig ret) {
        String propertyName = parseAttribute(e, "name");
        String type = parseAttribute(e, "type");
        String value = parseAttribute(e, "value");
        if ("refBean".equals(type)) {
            ret.regRefBeanId(propertyName, value);
        } else if ("String".equals(type)) {
            ret.regPropertiesValue(propertyName, value);
        }
        return ret;
    }

    /**
     * @param e
     * @param ret
     */
    private IXMLBeanConfig parseBeanClassValue(Element e, IXMLBeanConfig ret) {
        String className = parseAttribute(e, "class");
        Class implClass = null;
        try {
            implClass = ClassLoaderUtil.findClass(className);
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ret.setBeanClass(implClass);
        return ret;
    }

    /**
     * @param e
     * @param ret
     */
    protected IXMLBeanConfig parseBeanIdValue(Element e, IXMLBeanConfig ret) {
        String beanId = parseAttribute(e, "id");
        ret.setBeanId(beanId);
        return ret;
    }

    /**
     * @param e
     * @param ret
     */
    protected IXMLBeanConfig parseSingletonValue(Element e, IXMLBeanConfig ret) {
        String singleton = parseAttribute(e, "singleton");
        ret.setSingleton(singleton);
        return ret;
    }

    /**
     * @param e
     * @param ret
     * @return
     */
    protected IXMLBeanConfig parseOtherAttribute(Element e, IXMLBeanConfig ret) {
        // String enabledBeanIds = parseAttribute(e, "enabledBeanIds");
        // ret.regPropertiesValue("enabledBeanIds", enabledBeanIds);
        return ret;
    }

    protected IXMLBeanConfig getXMLBeanConfigImpl() {
        return new XMLBeanConfig();
    }
}
