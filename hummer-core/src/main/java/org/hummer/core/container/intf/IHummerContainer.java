package org.hummer.core.container.intf;

import org.hummer.core.config.intf.IConfigManager;

public interface IHummerContainer {
    public static final String DS_POOL_DRUID = "druid";
    public static final String DS_POOL_HIKARI = "hikari";

    IBusinessServiceManager getServiceManager();

    String getDataSourcePoolType();

    void reInit();

    IConfigManager getConfigManager();
}
