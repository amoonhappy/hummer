package org.hummer.core.container.impl;

import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.factory.impl.XMLBeanFactory;

public class BusinessServiceManager implements IBusinessServiceManager {
    private static IBusinessServiceManager instance = new BusinessServiceManager();

    /**
     * disable manually creation
     */
    private BusinessServiceManager() {
    }

    protected static IBusinessServiceManager getInstance() {
        return instance;
    }

    public Object getService(String serviceName) {
        return XMLBeanFactory.getInstance().getBean(serviceName);
    }

    public void reInit() {
        XMLBeanFactory.getInstance().reInit();
    }
}
