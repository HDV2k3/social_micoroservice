package com.example.connect_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ConnectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectServiceApplication.class, args);
	}

}
