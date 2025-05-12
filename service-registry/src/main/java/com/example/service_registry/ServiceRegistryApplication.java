package com.example.service_registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

	public static void main(String[] args) {

		SpringApplication.run(ServiceRegistryApplication.class, args);
	}

}
//question service register itself on eureka server and quiz service will be able to search it