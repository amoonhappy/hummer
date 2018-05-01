package org.hummer.core.container.intf;

import org.hummer.core.config.intf.IConfiguration;

public interface IConfigurationManager {
    public IConfiguration getConfiguration(String nameSpace);
}