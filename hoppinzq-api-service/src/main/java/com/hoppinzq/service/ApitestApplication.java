package com.hoppinzq.service;

import com.hoppinzq.service.aop.annotation.EnableServiceCache;
import com.hoppinzq.service.aop.annotation.EnableServiceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableServiceCache
@EnableAsync
//@EnableServiceRegister
@SpringBootApplication
@ServletComponentScan
public class ApitestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApitestApplication.class, args);
	}

}
