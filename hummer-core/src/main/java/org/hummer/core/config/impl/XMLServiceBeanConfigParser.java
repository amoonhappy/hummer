package org.hummer.core.config.impl;

import org.dom4j.Element;
import org.hummer.core.config.intf.IXMLBeanConfig;
import org.hummer.core.config.intf.IXMLBusinessServiceConfig;

//TODO: to implement using separated models?
@Deprecated
public class XMLServiceBeanConfigParser extends XMLBeanConfigParser {
    protected IXMLBeanConfig getXMLBeanConfigImpl() {
        return new XMLBizServiceConfig();
    }

    protected IXMLBeanConfig parseOtherAttribute(Element e, IXMLBeanConfig ret) {
        return parseTransactionAttr(e, ret);
    }

    private IXMLBeanConfig parseTransactionAttr(Element e, IXMLBeanConfig ret) {
        String transType = parseAttribute(e, "transType");
        IXMLBusinessServiceConfig ret1 = (IXMLBusinessServiceConfig) ret;
        ret1.setTransactionType(transType);
        return ret1;
    }
}