package org.hummer.core.container.impl;

import org.hummer.core.config.impl.CPConfigManager;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IHummerContainer;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

public class HummerContainer implements IHummerContainer {
    private static final Logger log = Log4jUtils.getLogger(HummerContainer.class);
    private static IHummerContainer instance = new HummerContainer();
    private ApplicationContext ctx = null;

    IBusinessServiceManager serviceManager;
    CPConfigManager configManager;
    //Default DS is druid
    String dsType = IHummerContainer.DS_POOL_DRUID;

    private HummerContainer() {
        init();
    }

    public static IHummerContainer getInstance() {
        return instance;
    }

    public CPConfigManager getConfigManager() {
        return configManager;
    }

    public void reInit() {
        configManager.reInit();
        serviceManager.reInit();
        init();
    }

    /**
     * initialization of Business Service Manager
     */
    private void init() {
        serviceManager = BusinessServiceManager.getInstance();
        configManager = CPConfigManager.getInstance();
        IConfiguration archConfig = configManager.getHummerMainCfg();
        if (archConfig != null) {
            String db_conn_pool_type = (String) archConfig.getValue(CPConfigManager.HUMMER_DB_CONN_POOL_TYPE);
            if (db_conn_pool_type != null) {
                log.info("DB connection pool type is: [{}]", db_conn_pool_type);
            } else {
                db_conn_pool_type = IHummerContainer.DS_POOL_DRUID;
                log.info("No DB connection pool type found in Hummer's main config! using default type: [{}]", db_conn_pool_type);
            }
            this.dsType = db_conn_pool_type;
        }
        //read ds config
    }

    @Override
    public void bondWithSpringContext(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Object getBeanFromSpring(String beanName) {
        if (StringUtil.isEmpty(beanName) || this.ctx == null) {
            return null;
        } else {
            return this.ctx.getBean(beanName);
        }
    }

    public IBusinessServiceManager getServiceManager() {
        return serviceManager;
    }

    public String getDataSourcePoolType() {
        return dsType;
    }
}
