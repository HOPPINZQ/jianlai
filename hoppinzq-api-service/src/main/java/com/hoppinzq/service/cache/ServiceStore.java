package com.hoppinzq.service.cache;

import com.hoppinzq.service.service.ServiceWrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:ZhangQi
 * 注册中心缓存
 **/
public class ServiceStore{
    public static List<ServiceWrapper> serviceWrapperList = new ArrayList<>();
    public static Map<String, Object> serviceMap = new HashMap<>();
}
