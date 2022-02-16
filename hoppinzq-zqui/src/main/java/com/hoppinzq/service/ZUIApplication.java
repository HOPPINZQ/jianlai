package com.hoppinzq.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: zq
 */
@SpringBootApplication
@ServletComponentScan
public class ZUIApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ZUIApplication.class, args);
    }
}
