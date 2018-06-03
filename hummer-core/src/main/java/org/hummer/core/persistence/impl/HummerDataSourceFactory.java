package org.hummer.core.persistence.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IHummerContainer;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author Jeff Zhou
 * @date
 */
public class HummerDataSourceFactory extends UnpooledDataSourceFactory {
    final static Logger log = Log4jUtils.getLogger(HummerDataSourceFactory.class);
    IHummerContainer hummerContainer = HummerContainer.getInstance();
    String type = hummerContainer.getDataSourcePoolType();
//    public static HummerDataSourceFactory hummerDataSourceFactory;

    public HummerDataSourceFactory() {
        log.info("Hummer DS Pool Type is: [{}]", type);
        try {
            if (type != null && IHummerContainer.DS_POOL_DRUID.equals(type)) {
                this.dataSource = new DruidDataSource();
                log.debug("Created DS of [{}]", type);
            } else if (type != null && IHummerContainer.DS_POOL_HIKARI.equals(type)) {
                this.dataSource = new HikariDataSource();
                log.debug("Created DS of [{}]", type);
            } else {
                this.dataSource = new PooledDataSource();
            }
            if (type != null && (IHummerContainer.DS_POOL_DRUID.equals(type) || IHummerContainer.DS_POOL_HIKARI.equals(type))) {
                Properties properties = new Properties();
                InputStream in = IHummerContainer.class.getClassLoader().getResourceAsStream("ds_" + type + ".properties");
                properties.load(in);
                this.setProperties(properties);
                log.info("Loading : [ds_{}.properties]", type);
            } else {
                Properties properties = new Properties();
                InputStream in = IHummerContainer.class.getClassLoader().getResourceAsStream("ds_default.properties");
                properties.load(in);
                this.setProperties(properties);
                log.info("Loading : [ds_default.properties]");
            }
        } catch (Exception e) {
            log.error("create datasource failed!", e);
            throw new RuntimeException("create datasource failed!");
        }
    }
}