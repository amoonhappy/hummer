package org.hummer.core.persistence.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IHummerContainer;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

public class HummerDataSourceFactory extends UnpooledDataSourceFactory {
    final static Logger log = Log4jUtils.getLogger(HummerDataSourceFactory.class);
    IHummerContainer hummerContainer = HummerContainer.getInstance();
    String type = hummerContainer.getDataSourcePoolType();

    public HummerDataSourceFactory() {
        log.info("Hummer DS Pool Type is: [{}]", type);
        if (type != null && IHummerContainer.DS_POOL_DRUID.equals(type)) {
            this.dataSource = new DruidDataSource();
            log.debug("Created DS of [{}]", type);
        } else if (type != null && IHummerContainer.DS_POOL_HIKARI.equals(type)) {
            this.dataSource = new HikariDataSource();
            log.debug("Created DS of [{}]", type);
        } else {
            this.dataSource = new DruidDataSource();
        }
    }
}
