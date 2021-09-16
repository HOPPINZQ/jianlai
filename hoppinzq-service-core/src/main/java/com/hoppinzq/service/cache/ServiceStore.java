package com.hoppinzq.service.cache;

import com.hoppinzq.service.service.ServiceWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author:ZhangQi
 * 注册中心服务存放
 **/
public class ServiceStore {
    public static List<ServiceWrapper> serviceWrapperList = Collections.synchronizedList(new ArrayList<ServiceWrapper>());
}
