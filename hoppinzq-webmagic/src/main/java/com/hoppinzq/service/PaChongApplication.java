package com.hoppinzq.service;

import com.hoppinzq.service.service.ZhihuTask;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@SpringBootApplication
@MapperScan(basePackages="com.hoppinzq.service.dao")
public class PaChongApplication implements CommandLineRunner {

	@Autowired
	private ZhihuTask zhihuTask;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(PaChongApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		// 爬取知乎数据
		zhihuTask.crawl();
	}

}
