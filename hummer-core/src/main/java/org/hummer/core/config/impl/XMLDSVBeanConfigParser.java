package org.hummer.core.config.impl;

import org.dom4j.Element;
import org.hummer.core.config.intf.IXMLBeanConfig;

//TODO: to implement using separated models?
public class XMLDSVBeanConfigParser extends XMLBeanConfigParser {
    protected IXMLBeanConfig getXMLBeanConfigImpl() {
        return new XMLDataServiceConfig();
    }

    protected IXMLBeanConfig parseOtherAttribute(Element e, IXMLBeanConfig ret) {
        return ret;
    }
    /*
     * protected IXMLBeanConfig parseTransactionAttr(Element e, IXMLBeanConfig
     * ret) { String transType = parseAttribute(e, "transType");
     * IXMLBusinessServiceConfig ret1 = (IXMLBusinessServiceConfig) ret;
     * ret1.setTransactionType(transType); return ret1; }
     */
}
