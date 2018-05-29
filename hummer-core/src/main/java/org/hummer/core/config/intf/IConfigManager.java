package org.hummer.core.config.intf;

import java.util.Map;

public interface IConfigManager {

    public static final String HUMMER_COMPONENT_ID = "hummer.component_id";
    public static final String HUMMER_COMPONENT_VER = "hummer.component_id.version";
    public static final String HUMMER_DB_CONN_POOL_TYPE = "hummer.db_conn_pool_type";

    public static final String HUMMER_COMPONENT_REPLACEMENT = "component_id";
    public static final String HUMMER_CFG_MAIN_PROPERTIES = "hummer-cfg-main.properties";
    public static final String JOINER = "-";
    public static final String CONFIG = "cfg";
    public static final String CORE_CONFIG_PATH_PREFIX = "core/config/";
    public static final String LOCAL_CONFIG_PATH_PREFIX = "local/config/";
    public static final String PATH_SEPERATOR = "/";

    public abstract IXMLConfiguration getXMLConfig(String fileName, String serviceName);

    public abstract Map<String, IXMLConfiguration> getAllXMLConfiguration();
}