package com.hoppinzq.service.config;

import com.hoppinzq.service.servlet.ProxyServlet;
import com.hoppinzq.service.servlet.SpringProxyServlet;
import com.hoppinzq.service.util.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册 注册服务Servlet
 */
@Configuration
public class ServeltConfig {
    private static Logger logger = LoggerFactory.getLogger(ServeltConfig.class);

    @Value("${zqServer.prefix:/service}")
    private String prefix;

    @Value("${server.port:8803}")
    private String port;

    @Bean
    public ServletRegistrationBean ServletRegistrationBean(){
        ProxyServlet proxyServlet=new SpringProxyServlet();
        ServletRegistrationBean registration = new ServletRegistrationBean(proxyServlet,prefix);
        logger.debug("注册服务servlet，服务路径：http://127.0.0.1:"+port+prefix);
        return registration;
    }
}
