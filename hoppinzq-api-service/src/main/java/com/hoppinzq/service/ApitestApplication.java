package com.hoppinzq.service;

import com.hoppinzq.service.aop.annotation.EnableMyCache;
import com.hoppinzq.service.aop.annotation.EnableServiceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableMyCache
@EnableAsync
@EnableServiceRegister
@SpringBootApplication
@ServletComponentScan
public class ApitestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApitestApplication.class, args);
	}

}
