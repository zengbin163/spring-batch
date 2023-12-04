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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.zengbin.batch.mapper.writer", sqlSessionFactoryRef = "writerSessionFactory")
public class DbWriterConfiguration {

    private static final String MAPPER_PATH = "classpath:mapper/writer/*.xml";
    private static final String ENTITY_PACKAGE = "com.zengbin.batch.vo.writer";


    @Bean(name = "writerDataSource")
    @Qualifier("writerDataSource")
    @ConfigurationProperties(prefix = "writer.datasource")
    public DataSource writerDataSource(){
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "writerSessionFactory")
    @Qualifier("writerSessionFactory")
    public SqlSessionFactory writerSessionFactory(@Qualifier("writerDataSource") DataSource writerDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean ();
        sessionFactory.setDataSource(writerDataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources(MAPPER_PATH));
        sessionFactory.setTypeAliasesPackage(ENTITY_PACKAGE);
        return sessionFactory.getObject();
    }

    @Bean(name = "writerSqlSessionTemplate")
    @Qualifier("writerSqlSessionTemplate")
    public SqlSessionTemplate writerSqlSessionTemplate(@Qualifier("writerSessionFactory") SqlSessionFactory writerSessionFactory) {
        SqlSessionTemplate template = new SqlSessionTemplate(writerSessionFactory);
        return template;
    }

}
