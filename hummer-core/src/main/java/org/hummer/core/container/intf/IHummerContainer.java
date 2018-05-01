package org.hummer.core.container.intf;

public interface IHummerContainer {
    /**
     * get the container instance.
     *
     * @return
     */

    public IBusinessServiceManager getServiceManager();

    public IConfigurationManager getConfigManager();

}
