package com.bonifacio.urls_ripper.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

public class DataBaseConfig {
    @Autowired
    Environment env;
    @Bean
    public DataSource dataSource(){
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("driverClassName")));
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("url")));
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("user")));
        dataSource.setDriverClassName(Objects.requireNonNull(Objects.requireNonNull(env.getProperty("password"))));
        return dataSource;
    }
}
