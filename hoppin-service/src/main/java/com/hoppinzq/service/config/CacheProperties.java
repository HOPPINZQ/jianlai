package com.hoppinzq.service.config;//package com.hoppinzq.service.config;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.InputStream;
//import java.util.Iterator;
//import java.util.Properties;
//
///**
// * @author:ZhangQi
// **/
//@Configuration
//public class CacheProperties implements InitializingBean {
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        InputStream is=CacheProperties.class.getClassLoader().getResourceAsStream("application.properties");
//        Properties pro=new Properties();
//        pro.load(is);
//        Iterator iterator = pro.keySet().iterator();
//        while (iterator.hasNext()) {
//            //System.out.println(iterator.next());
//            //System.out.println(pro.getProperty(iterator.next().toString()));
//        }
//    }
//}
