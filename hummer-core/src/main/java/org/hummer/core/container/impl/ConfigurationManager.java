package org.hummer.core.container.impl;

import org.apache.commons.collections.FastHashMap;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.container.intf.IConfigurationManager;
import org.hummer.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Deprecated
public class ConfigurationManager implements IConfigurationManager {
    private static final String JOINER = "-";

    private static final String CONFIG = "cfg";

    private static final String PROP_FILEEXT = ".properties";

    private static final String XML_FILEEXT = ".xml";

    private static List<String> reloadList = new ArrayList<String>();

    Map configurationMap = new FastHashMap();

    /**
     * @param nameSpace the format is componentId.domain, for example:
     *                  hummer-web.testing
     */
    public IConfiguration getConfiguration(String nameSpace) {
        String[] arrays = StringUtil.joinArray(nameSpace, ".");
        String componentId = arrays[0];
        String domain = arrays[1];
        IConfiguration ret = (IConfiguration) configurationMap.get(nameSpace);
        if (ret == null || reloadList.contains(nameSpace)) {
            ret = parseProperties(componentId, domain);
        }

        return null;
    }

    private IConfiguration parseProperties(String componentId, String domain) {
        IConfiguration ret = null;
        if (!StringUtil.isEmpty(componentId) && !StringUtil.isEmpty(domain)) {
            String propFileName = getPropertiesFileName(componentId, domain);
            String xmlFileName = getXMLFileName(componentId, domain);

        }

        return ret;
    }

    private String getXMLFileName(String componentId, String domain) {
        StringBuffer sb = new StringBuffer().append(componentId).append(JOINER).append(CONFIG).append(JOINER).append(
                domain).append(XML_FILEEXT);
        return sb.toString();
    }

    private String getPropertiesFileName(String componentId, String domain) {
        StringBuffer sb = new StringBuffer().append(componentId).append(JOINER).append(CONFIG).append(JOINER).append(
                domain).append(PROP_FILEEXT);
        return sb.toString();
    }
}
