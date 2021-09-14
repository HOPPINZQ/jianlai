package com.hoppinzq.service.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.*;

@Component
public class WebRegisterServletConfig implements InitializingBean, ApplicationContextAware, ServletContextListener {
    /**
     * 上下文对象实例
     */
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ServletContext servletContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
    @Override
    public void afterPropertiesSet() throws Exception {
//        ProxyServlet servlet=new ProxyServlet();
//        ServletRegistration.Dynamic dynamic=servletContext.addServlet("proxyServlet",servlet);
//        dynamic.setLoadOnStartup(1);
//        dynamic.addMapping("/zc");
    }
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException{
//        ProxyServlet servlet=new ProxyServlet();
//        ServletRegistration.Dynamic dynamic=servletContext.addServlet("proxyServlet","servlet");
//        servletContext.setRequestCharacterEncoding("UTF-8");
//        servletContext.setResponseCharacterEncoding("UTF-8");
//        dynamic.setLoadOnStartup(1);
//        dynamic.addMapping("/zc");
//    }
}
