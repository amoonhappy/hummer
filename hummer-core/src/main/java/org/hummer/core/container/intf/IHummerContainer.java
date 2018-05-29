package org.hummer.core.container.intf;

public interface IHummerContainer {
    public static final String DS_POOL_DRUID = "druid";
    public static final String DS_POOL_HIKARI = "hikari";
    IBusinessServiceManager getServiceManager();

    String getDataSourcePoolType();
}
