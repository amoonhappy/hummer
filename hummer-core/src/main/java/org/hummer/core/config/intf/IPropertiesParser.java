package org.hummer.core.config.intf;

import java.io.FileNotFoundException;

public interface IPropertiesParser {
//    public static final String CONFIG_TYPE_ALL = "ALL";
//
//    public static final String CONFIG_TYPE_BSV = "BSV";
//
//    public static final String CONFIG_TYPE_DSV = "DSV";
//
//    public static final String CONFIG_TYPE_OTHER = "OTHER";

    IConfiguration parse(String fileName) throws FileNotFoundException;
}
