package org.hummer.core.persistence.impl;

import org.mybatis.spring.SqlSessionFactoryBean;

import javax.sql.DataSource;

public class HummerSqlSessionFactoryBean extends SqlSessionFactoryBean {

    @Override
    public void setDataSource(DataSource dataSource) {
        HummerDataSourceFactory hummerDataSourceFactory = new HummerDataSourceFactory();
        dataSource = hummerDataSourceFactory.getDataSource();
        super.setDataSource(dataSource);
    }
}
