package com.zengbin.batch.configure;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DbBatchConfiguration {

    @Bean(name = "batchDataSource")
    @Qualifier("batchDataSource")
    @ConfigurationProperties(prefix = "batch.datasource")
    @Primary
    public DataSource batchDataSource(){
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

}
