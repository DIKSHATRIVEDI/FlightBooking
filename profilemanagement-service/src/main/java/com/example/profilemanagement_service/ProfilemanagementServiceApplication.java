package com.example.profilemanagement_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProfilemanagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfilemanagementServiceApplication.class, args);
	}

}
