package org.hummer.core.config.intf;

import java.util.Map;

public interface IXMLConfig extends IConfiguration {
    public static final String TYPE_BIZ_SERVICE = "bsv";

    public static final String TYPE_DATA_SERVICE = "dsv";

    public static final String TYPE_BEAN = "bean";

    public void setChild(Map<String, IXMLConfig> child);

    public Map<String, IXMLConfig> getChildMap();
}
