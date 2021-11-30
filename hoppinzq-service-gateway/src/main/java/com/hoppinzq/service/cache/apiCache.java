package com.hoppinzq.service.cache;


import com.hoppinzq.service.core.ApiRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author:ZhangQi
 * 注册在网关的服务接口
 **/
public class apiCache {

    //供内部调用api缓存,在项目启动前会将api映射存入这里
    public static Map<String, ApiRunnable> apiMap =  new ConcurrentHashMap<String, ApiRunnable>();

    //供外部查看api缓存，在项目启动前会将暴露的服务跟api存入这里
    public static List<Map> outApiList= Collections.synchronizedList(new ArrayList<Map>());


}
