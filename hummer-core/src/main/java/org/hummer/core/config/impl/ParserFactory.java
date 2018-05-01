package org.hummer.core.config.impl;

import org.hummer.core.config.intf.ICPPropConfigParser;
import org.hummer.core.config.intf.ICPXMLConfigParser;
import org.hummer.core.config.intf.IPropConfigParser;
import org.hummer.core.config.intf.IXMLConfigParser;

public class ParserFactory {
    public static final String PROP_FILEEXT = "properties";
    public static final String XML_FILEEXT = "xml";
    public static IPropConfigParser propConfigParser = new PropConfigParser();
    public static IXMLConfigParser xmlParser = new XMLBeanConfigParser();
    public static ICPPropConfigParser cpPropConfigParser = new CPPropConfigParser();
    public static ICPXMLConfigParser cpXMLConfigParser = new CPXMLConfigParser();

    private static ParserFactory pf = new ParserFactory();

    public static ParserFactory getInstance() {
        return pf;
    }

    public static IPropConfigParser getConfigParser() {
        return propConfigParser;
    }

    public static IXMLConfigParser getXMLConfigParser() {
        return xmlParser;
    }

    public static ICPPropConfigParser getCPPropConfigParser() {
        return cpPropConfigParser;
    }

    public static ICPXMLConfigParser getCPXMLConfigParser() {
        return cpXMLConfigParser;
    }
}
