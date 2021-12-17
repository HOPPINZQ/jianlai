package com.hoppinzq.service.cache;

import com.hoppinzq.service.bean.ServiceWrapper;

import java.util.*;

/**
 * @author:ZhangQi
 * 注册中心服务存放
 **/
public class ServiceStore {
    //服务缓存
    public static List<ServiceWrapper> serviceWrapperList = Collections.synchronizedList(new ArrayList<ServiceWrapper>());
    //心跳服务缓存，仅注册中心调用
    public static List<ServiceWrapper> heartbeatService=new ArrayList<>();
}
