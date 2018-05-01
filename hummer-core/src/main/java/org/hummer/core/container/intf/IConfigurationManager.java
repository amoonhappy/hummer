package org.hummer.core.container.intf;

import org.hummer.core.config.intf.IConfiguration;

@Deprecated
public interface IConfigurationManager {
    public IConfiguration getConfiguration(String nameSpace);
}