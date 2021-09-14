package com.hoppinzq.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 配置自动打开浏览器
 * @author:ZhangQi
 **/
@Component
@Order(10)
public class CommandRunner implements CommandLineRunner {

    @Value("${server.port}")
    private String port;


    @Override
    public void run(String... args) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if(os.contains("windows")) {
                // 默认浏览器打开
                //Runtime.getRuntime().exec("cmd   /c   start   http://127.0.0.1:" + port + "/");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //log.error("打开默认浏览器异常", ex);
        }
    }
}
