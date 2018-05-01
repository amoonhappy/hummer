package org.hummer.core.container.impl;

import org.hummer.core.config.impl.CPConfigManager;
import org.hummer.core.config.intf.IConfigManager;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.factory.impl.XMLBeanFactory;

public class BusinessServiceManager implements IBusinessServiceManager {
    private static IBusinessServiceManager instance = new BusinessServiceManager();
    IConfigManager cm = CPConfigManager.getInstance();

    /**
     * disable mannually creation
     */
    private BusinessServiceManager() {
    }

    public static IBusinessServiceManager getInstance() {
        return instance;
    }

    public Object getService(String serviceName) {
        Object ret = XMLBeanFactory.getInstance().getBean(serviceName);
        return ret;
    }
}
