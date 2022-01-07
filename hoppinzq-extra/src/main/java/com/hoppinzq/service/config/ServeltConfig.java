package com.hoppinzq.service.config;

import com.hoppinzq.service.bean.PropertyBean;
import com.hoppinzq.service.servlet.ProxyServlet;
import com.hoppinzq.service.servlet.SpringProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册 注册服务Servlet
 */
@Configuration
public class ServeltConfig implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(ServeltConfig.class);

    @Autowired
    private PropertyBean propertyBean;

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Bean
    public ServletRegistrationBean ServletRegistrationBean(){
        ProxyServlet proxyServlet=new SpringProxyServlet();
        proxyServlet.setApplicationContext(this.applicationContext);
        String serviceAddress="http://"+ propertyBean.getIp() +":"+propertyBean.getPort()+propertyBean.getPrefix();
        proxyServlet.setPropertyBean(propertyBean);
        ServletRegistrationBean registration = new ServletRegistrationBean(proxyServlet,propertyBean.getPrefix());
        logger.debug("注册服务servlet，服务路径："+serviceAddress);
        return registration;
    }

}
