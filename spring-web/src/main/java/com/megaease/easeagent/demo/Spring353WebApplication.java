package com.megaease.easeagent.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Spring353WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spring353WebApplication.class, args);
	}

}
