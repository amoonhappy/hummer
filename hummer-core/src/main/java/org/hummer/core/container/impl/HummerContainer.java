package org.hummer.core.container.impl;

import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IConfigurationManager;
import org.hummer.core.container.intf.IHummerContainer;

public class HummerContainer implements IHummerContainer {

    private static HummerContainer instance = new HummerContainer();
    IBusinessServiceManager serviceManager;
    IConfigurationManager configManager;

    private HummerContainer() {
        init();
    }

    public static IHummerContainer getInstance() {
        return instance;
    }

    /**
     * initialization of Business Service Manager
     */
    private void init() {

    }

    public IBusinessServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(IBusinessServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public IConfigurationManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(IConfigurationManager configManager) {
        this.configManager = configManager;
    }

}
