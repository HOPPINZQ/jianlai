package com.hoppinzq.service.plugin;

import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.client.ServiceProxyFactory;
import com.hoppinzq.service.Proxy.cglib.CglibServiceCacheProxy;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;


/**
 * @author:ZhangQi
 **/
public class ServiceCacheFilter implements InitializingBean {
    @Resource
    ServiceProxyFactory serviceProxyFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        CglibServiceCacheProxy cglibProxy = new CglibServiceCacheProxy();
        ServiceProxyFactory proxy = (ServiceProxyFactory)cglibProxy.getProxy(serviceProxyFactory);
        ServiceStore.serviceProxyFactory=proxy;
    }
}
