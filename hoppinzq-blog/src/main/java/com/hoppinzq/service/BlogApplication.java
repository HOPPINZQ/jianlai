package com.hoppinzq.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

//@EnableGateway//默认网关开关注解
@EnableAsync
@EnableCaching
@SpringBootApplication
@ServletComponentScan
@MapperScan(basePackages = "com.hoppinzq.service.dao")
public class BlogApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(BlogApplication.class, args);
    }

}
