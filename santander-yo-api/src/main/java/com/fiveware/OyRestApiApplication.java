package com.fiveware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OyRestApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(OyRestApiApplication.class, args);
	}
}