package com.hoppinzq.service.runner;

import com.hoppinzq.service.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)//越小越先执行
public class CacheRunner implements ApplicationRunner {

    @Autowired
    private BlogService blogService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}