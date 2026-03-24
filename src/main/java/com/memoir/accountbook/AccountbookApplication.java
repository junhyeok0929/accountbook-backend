package com.memoir.accountbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EntityScan("com.memoir.accountbook")
public class AccountbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountbookApplication.class, args);
	}

}
