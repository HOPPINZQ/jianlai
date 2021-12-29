package com.hoppinzq.service;

import com.hoppinzq.service.aop.annotation.EnableGateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@EnableGateway
@SpringBootApplication
@ServletComponentScan
public class AuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}
