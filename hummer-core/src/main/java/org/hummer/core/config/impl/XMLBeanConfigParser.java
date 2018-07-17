package org.hummer.core.config.impl;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.hummer.core.config.intf.IXMLBeanConfig;
import org.hummer.core.config.intf.IXMLConfigParser;
import org.hummer.core.config.intf.IXMLConfig;
import org.hummer.core.util.ClassLoaderUtil;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLBeanConfigParser implements IXMLConfigParser {
    private static Logger log = Log4jUtils.getLogger(XMLBeanConfigParser.class);

    public Map<String, IXMLConfig> parse(Element xmlDoc) {
        Map<String, IXMLConfig> ret = new HashMap<>();

        List beans = xmlDoc.elements();
        for (Object bean1 : beans) {
            Element bean = (Element) bean1;
            IXMLBeanConfig xmlBeanConfig = parseBeanConfig(bean);
            ret.put(xmlBeanConfig.getBeanId(), xmlBeanConfig);
        }
        return ret;
    }

    String parseAttribute(Element e, String attrName) {
        String value = null;
        if (e != null && !StringUtil.isEmpty(attrName)) {
            value = StringUtils.trimToEmpty(e.attributeValue(attrName));
        }
        return value;
    }

    private IXMLBeanConfig parseBeanConfig(Element e) {
        IXMLBeanConfig ret = getXMLBeanConfigImpl();
        ret = parseBeanClassValue(e, ret);
        ret = parseBeanIdValue(e, ret);
        ret = parseSingletonValue(e, ret);
        ret = parsePropertiesValue(e, ret);
        ret = parseOtherAttribute(e, ret);
        ret = parseAutowiredValue(e, ret);
        return ret;
    }

    private IXMLBeanConfig parsePropertiesValue(Element e, IXMLBeanConfig ret) {
        if (e != null && ret != null) {
            List properties = e.elements("properties");
            for (Object property1 : properties) {
                Element property = (Element) property1;
                ret = parseBeanAttrConfig(property, ret);
            }
        }
        return ret;
    }

    private IXMLBeanConfig parseBeanAttrConfig(Element e, IXMLBeanConfig ret) {
        String propertyName = parseAttribute(e, "name");
        String type = parseAttribute(e, "type");
        String value = parseAttribute(e, "value");
        if ("refBean".equals(type)) {
            ret.regRefBeanId(propertyName, value);
        } else if ("String".equals(type)) {
            ret.regPropertiesValue(propertyName, value);
        } else if ("springBean".equals(type)) {
            ret.regSpringBeanId(propertyName, value);
        }
        return ret;
    }

    /**
     * @param e   Element
     * @param ret IXMLBeanConfig
     */
    private IXMLBeanConfig parseBeanClassValue(Element e, IXMLBeanConfig ret) {
        String className = parseAttribute(e, "class");
        Class implClass = null;
        try {
            implClass = ClassLoaderUtil.findClass(className);
        } catch (ClassNotFoundException e1) {
            log.error("parseBeanClassValue error! Class:[{}] doesn't exist!", className, e1);
        }
        ret.setBeanClass(implClass);
        return ret;
    }

    /**
     * @param e   Element
     * @param ret IXMLBeanConfig
     */
    private IXMLBeanConfig parseBeanIdValue(Element e, IXMLBeanConfig ret) {
        String beanId = parseAttribute(e, "id");
        ret.setBeanId(beanId);
        return ret;
    }

    /**
     * @param e   Element
     * @param ret IXMLBeanConfig
     */
    private IXMLBeanConfig parseSingletonValue(Element e, IXMLBeanConfig ret) {
        String singleton = parseAttribute(e, "singleton");
        ret.setSingleton(singleton);
        return ret;
    }

    /**
     * @param e   Element
     * @param ret IXMLBeanConfig
     */
    private IXMLBeanConfig parseAutowiredValue(Element e, IXMLBeanConfig ret) {
        String autowired = parseAttribute(e, "autowired");
        ret.setAutowired(autowired);
        return ret;
    }

    /**
     * @param e   Element
     * @param ret IXMLBeanConfig
     * @return IXMLBeanConfig
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
