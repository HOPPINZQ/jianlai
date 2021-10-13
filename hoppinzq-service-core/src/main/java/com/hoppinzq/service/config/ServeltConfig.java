package com.hoppinzq.service.config;

import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.serviceImpl.PropertyBean;
import com.hoppinzq.service.servlet.ProxyServlet;
import com.hoppinzq.service.servlet.SpringProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 注册 注册服务Servlet
 */
@Configuration
public class ServeltConfig {
    private static Logger logger = LoggerFactory.getLogger(ServeltConfig.class);

    @Autowired
    private PropertyBean propertyBean;

    @Bean
    public ServletRegistrationBean ServletRegistrationBean(){
        ProxyServlet proxyServlet=new SpringProxyServlet();
        proxyServlet.setServiceWrappers(ServiceStore.serviceWrapperList);
        String serviceAddress="http://"+ propertyBean.getIp() +":"+propertyBean.getPort()+propertyBean.getPrefix();
        proxyServlet.setPropertyBean(propertyBean);
        ServletRegistrationBean registration = new ServletRegistrationBean(proxyServlet,propertyBean.getPrefix());
        logger.debug("注册服务servlet，服务路径："+serviceAddress);
        return registration;
    }


}
