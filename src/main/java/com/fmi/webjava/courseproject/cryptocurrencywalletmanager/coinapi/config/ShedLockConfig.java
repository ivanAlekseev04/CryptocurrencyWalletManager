package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.coinapi.config;

import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import net.javacrumbs.shedlock.core.LockProvider;

import javax.sql.DataSource;

@Configuration
public class ShedLockConfig {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {

        return new JdbcTemplateLockProvider(dataSource);
    }
}