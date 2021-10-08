package com.hoppinzq.service.cache;

import com.hoppinzq.service.serviceBean.ServiceWrapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:ZhangQi
 * 注册中心缓存
 **/
public class ServiceStore{
    public static List<ServiceWrapper> serviceWrapperList = Collections.synchronizedList(new ArrayList<ServiceWrapper>());
    public static Map<String, Object> serviceMap = new ConcurrentHashMap<>();

}
