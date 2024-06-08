package com.upgrade.meoku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class MeokuApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeokuApplication.class, args);
	}

}
