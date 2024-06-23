package com.fmi.webjava.courseproject.cryptocurrencywalletmanager;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service.CoinApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Executes when the ApplicationContext get initialized or refreshed
    @EventListener(ContextRefreshedEvent.class)
    public void deleteShedLockFromDB() {
        String query = "DELETE FROM public.shedlock WHERE name='CoinApi'";
        jdbcTemplate.update(query);
    }
}
