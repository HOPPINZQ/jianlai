package com.hoppinzq.service.config;

import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.serviceImpl.PropertyBean;
import com.hoppinzq.service.servlet.ProxyServlet;
import com.hoppinzq.service.util.SpringUtils;
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
public class ServletRegisterConfig{
    private static Logger logger = LoggerFactory.getLogger(ServletRegisterConfig.class);

    @Autowired
    private PropertyBean propertyBean;

    @Bean
    public ServletRegistrationBean ServletRegistrationBean(){
        ProxyServlet proxyServlet=new SpringProxyServlet();
        //必须设置一个保存服务的List，本地注册的服务实际上是保存在本地，并在注册中心注册一份服务存根
        proxyServlet.setServiceWrappers(ServiceStore.serviceWrapperList);
        //必须设置applicationContext以获取注册的服务的bean
        String serviceAddress="http://"+ propertyBean.getIp() +":"+propertyBean.getPort()+propertyBean.getPrefix();
        proxyServlet.setPropertyBean(propertyBean);
        proxyServlet.setApplicationContext(SpringUtils.getApplicationContext());
        ServletRegistrationBean registration = new ServletRegistrationBean(proxyServlet,propertyBean.getPrefix());
        logger.debug("注册服务servlet，服务路径："+serviceAddress);
        return registration;
    }
}
