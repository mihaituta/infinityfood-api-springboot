package com.tm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class InfinityfoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfinityfoodApplication.class, args);
	}
}
