package org.hummer.core.config.intf;

import org.dom4j.Element;

import java.util.Map;

public interface IXMLConfigParser {
    public Map<String, IXMLConfig> parse(Element xmlDoc);
}