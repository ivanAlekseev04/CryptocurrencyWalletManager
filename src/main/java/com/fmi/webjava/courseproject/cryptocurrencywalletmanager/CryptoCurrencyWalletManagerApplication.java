package com.fmi.webjava.courseproject.cryptocurrencywalletmanager;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT5M")
@SpringBootApplication
public class CryptoCurrencyWalletManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoCurrencyWalletManagerApplication.class, args);
	}
}
