package com.hoppinzq.service.cache;

import com.hoppinzq.service.client.ServiceProxyFactory;
import com.hoppinzq.service.service.ServiceWrapper;
import com.hoppinzq.service.util.SpringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:ZhangQi
 * 注册中心缓存
 **/
@Component
public class ServiceStore{
    public static List<ServiceWrapper> serviceWrapperList = new ArrayList<>();
    public static Map<String, Object> serviceMap = new HashMap<>();
    public static ServiceProxyFactory serviceProxyFactory = new ServiceProxyFactory();
}
