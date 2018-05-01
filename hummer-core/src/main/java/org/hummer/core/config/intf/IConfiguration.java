package org.hummer.core.config.intf;

public interface IConfiguration {
    Object getValue(String key);

    IConfiguration overwriteBy(IConfiguration localConfig);
}