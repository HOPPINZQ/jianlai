package com.hoppinzq.service.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;


/**
 * 错误页面配置
 */
@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.errorhtml");
       // ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/static/error/index.jsp");
        //ErrorPage e400 = new ErrorPage(HttpStatus.BAD_REQUEST, "/static/error/index.jsp");
       // registry.addErrorPages(e400 ,e404, e500);
        registry.addErrorPages(e404);
    }
}

