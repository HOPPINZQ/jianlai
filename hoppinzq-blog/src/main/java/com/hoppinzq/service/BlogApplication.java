package com.hoppinzq.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hoppinzq.service.aop.annotation.EnableGateway;
import com.hoppinzq.service.dao.BlogDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableGateway
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
