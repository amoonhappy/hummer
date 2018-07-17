package org.hummer.core.config.impl;

import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hummer.core.config.intf.IXMLConfig;
import org.hummer.core.config.intf.IXMLConfigParser;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.hummer.core.util.XmlDOMUtil;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class XMLConfigFactory {
    public static XMLConfigFactory instance = new XMLConfigFactory();
    private static Logger log = Log4jUtils.getLogger(XMLConfigFactory.class);

    private XMLConfigFactory() {

    }

    public static XMLConfigFactory getInstance() {
        return instance;
    }

    public Map<String, IXMLConfig> parse(String fileName) {
        // load core config
        Map<String, IXMLConfig> coreConfig = loadCoreConfig(fileName);
        // load local config
        Map<String, IXMLConfig> localConfig = loadLocalConfig(fileName);
        if (coreConfig != null && localConfig == null) {
            return coreConfig;
        } else if (coreConfig == null && localConfig != null) {
            return localConfig;
        } else {
            assert coreConfig != null;
            Set<String> coreKeySet = coreConfig.keySet();
            Set<String> localKeySet = localConfig.keySet();
            List<String> mergedKeySet = (List<String>) CollectionUtils.union(coreKeySet, localKeySet);
            // List<IXMLConfig> ret = new
            // ArrayList<IXMLConfig>();
            Map<String, IXMLConfig> ret = new HashMap<>();
            for (String key : mergedKeySet) {
                IXMLConfig coreTemp = coreConfig.get(key);
                IXMLConfig localTemp = localConfig.get(key);
                if (coreTemp == null && localTemp != null) {
                    ret.put(key, localTemp);
                } else if (coreTemp != null && localTemp != null) {
                    coreTemp.overwriteBy(localTemp);
                    ret.put(key, coreTemp);
                } else {
                    ret.put(key, coreTemp);
                }
            }

            return ret;
        }
    }

    private Map<String, IXMLConfig> loadLocalConfig(String fileName) {
        String localFileName = HummerConfigManager.LOCAL_CONFIG_PATH_PREFIX + fileName;
        Map<String, IXMLConfig> ret = null;
        try {
            Document xmlDoc = getDocument(localFileName);
            if (xmlDoc != null) {
                ret = convertToIConfiguration(xmlDoc);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("load local config xml failed!", e);
        }
        return ret;
    }

    private Map<String, IXMLConfig> loadCoreConfig(String fileName) {
        String coreFileName = HummerConfigManager.CORE_CONFIG_PATH_PREFIX + fileName;
        Map<String, IXMLConfig> ret = null;
        try {
            Document xmlDoc = getDocument(coreFileName);
            ret = convertToIConfiguration(xmlDoc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("load core config xml failed!", e);
        }
        return ret;
    }

    private Document getDocument(String fileName) throws ParserConfigurationException, SAXException, IOException {
        return XmlDOMUtil.dom4jParse(fileName);
    }

    private Map<String, IXMLConfig> convertToIConfiguration(Document xmlDoc) {
        Map<String, IXMLConfig> ret;

        Element root = xmlDoc.getRootElement();
        String configType = root.attributeValue("type");
        ret = getParser(configType).parse(root);

        return ret;
    }

    private IXMLConfigParser getParser(String configType) {
        IXMLConfigParser ret = null;
        if (!StringUtil.isEmpty(configType)) {
            if (IXMLConfig.TYPE_BEAN.equals(configType)) {
                ret = new XMLBeanConfigParser();
            } else if (IXMLConfig.TYPE_BIZ_SERVICE.equals(configType)) {
                ret = new XMLServiceBeanConfigParser();
            } else if (IXMLConfig.TYPE_DATA_SERVICE.equals(configType)) {
                ret = new XMLDSVBeanConfigParser();
            } else {
                ret = new XMLBeanConfigParser();
            }
        }
        return ret;
    }

}
