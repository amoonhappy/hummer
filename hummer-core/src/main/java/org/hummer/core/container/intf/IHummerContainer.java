package org.hummer.core.container.intf;

import org.hummer.core.config.intf.IConfigManager;
import org.springframework.context.ApplicationContext;

public interface IHummerContainer {
    public static final String DS_POOL_DRUID = "druid";
    public static final String DS_POOL_HIKARI = "hikari";

    void bondWithSpringContext(ApplicationContext ctx);

    Object getBeanFromSpring(String beanName);

    IBusinessServiceManager getServiceManager();

    String getDataSourcePoolType();

    void reInit();

    IConfigManager getConfigManager();
}
