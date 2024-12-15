package com.healthcare.mailing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailingServiceApplication.class, args);
	}

}
