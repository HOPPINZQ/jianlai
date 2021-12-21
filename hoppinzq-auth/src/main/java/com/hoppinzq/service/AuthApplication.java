package com.hoppinzq.service;

import com.hoppinzq.service.bean.RPCPropertyBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@ServletComponentScan
public class AuthApplication {
	public static void main(String[] args) {
		ApplicationContext applicationContext =SpringApplication.run(AuthApplication.class, args);
		RPCPropertyBean rpcPropertyBean = applicationContext.getBean("RPCPropertyBean", RPCPropertyBean.class);
	}
}
