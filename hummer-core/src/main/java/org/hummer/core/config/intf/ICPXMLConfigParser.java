package org.hummer.core.config.intf;

import java.io.FileNotFoundException;
import java.util.Map;

public interface ICPXMLConfigParser {
    public static final String CONFIG_TYPE_ALL = "ALL";

    public static final String CONFIG_TYPE_BSV = "BSV";

    public static final String CONFIG_TYPE_DSV = "DSV";

    public static final String CONFIG_TYPE_OTHER = "OTHER";

    public Map<String, IXMLConfiguration> parse(String fileName) throws FileNotFoundException;
}
