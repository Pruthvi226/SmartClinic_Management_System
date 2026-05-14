package com.smartclinic.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.smartclinic.dao",
        "com.smartclinic.service",
        "com.smartclinic.util"
})
public class AppConfig {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(property("db.driver"));
        dataSource.setUrl(property("db.url"));
        dataSource.setUsername(property("db.username"));
        dataSource.setPassword(property("db.password"));
        dataSource.setInitialSize(integerProperty("db.pool.initialSize"));
        dataSource.setMaxTotal(integerProperty("db.pool.maxTotal"));
        dataSource.setMaxIdle(integerProperty("db.pool.maxIdle"));
        dataSource.setMinIdle(integerProperty("db.pool.minIdle"));
        return dataSource;
    }

    @Bean
    @SuppressWarnings("null")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.smartclinic.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", property("hibernate.dialect"));
        properties.put("hibernate.show_sql", property("hibernate.show_sql"));
        properties.put("hibernate.format_sql", property("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", property("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.cache.use_second_level_cache", property("hibernate.cache.use_second_level_cache"));
        properties.put("hibernate.cache.use_query_cache", property("hibernate.cache.use_query_cache"));
        properties.put("hibernate.cache.region.factory_class", property("hibernate.cache.region.factory_class"));
        
        return properties;
    }

    private String property(String key) {
        return environment.resolveRequiredPlaceholders(environment.getRequiredProperty(key));
    }

    private int integerProperty(String key) {
        return Integer.parseInt(property(key));
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
