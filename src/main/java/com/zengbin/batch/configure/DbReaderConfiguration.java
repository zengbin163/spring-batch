package com.zengbin.batch.configure;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.zengbin.batch.mapper.reader", sqlSessionFactoryRef = "readerSessionFactory")
public class DbReaderConfiguration {

    private static final String MAPPER_PATH = "classpath:mapper/reader/*.xml";
    private static final String ENTITY_PACKAGE = "com.zengbin.batch.vo.reader";

    @Bean(name = "readerDataSource")
    @Qualifier("readerDataSource")
    @ConfigurationProperties(prefix = "reader.datasource")
    public DataSource readerDataSource(){
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "readerSessionFactory")
    @Qualifier("readerSessionFactory")
    @Primary
    public SqlSessionFactory readerSessionFactory(@Qualifier("readerDataSource") DataSource readerDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean ();
        sessionFactory.setDataSource(readerDataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources(MAPPER_PATH));
        sessionFactory.setTypeAliasesPackage(ENTITY_PACKAGE);
        return sessionFactory.getObject();
    }

    @Bean(name = "readerSqlSessionTemplate")
    @Qualifier("readerSqlSessionTemplate")
    public SqlSessionTemplate readerSqlSessionTemplate(@Qualifier("readerSessionFactory") SqlSessionFactory readerSessionFactory) {
        SqlSessionTemplate template = new SqlSessionTemplate(readerSessionFactory);
        return template;
    }

}
