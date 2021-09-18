package com.hoppinzq.service.cache;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author:ZhangQi
 **/
public class ServiceCache<K, V> {

    private final Map<K,V> cache = new ConcurrentHashMap<K,V>();

    public synchronized V getService(K arg) throws InterruptedException {
        V result = cache.get(arg);
        if(result==null){
            System.err.println("缓存没有值,赋值");
            result = (V)"zhangqi";
            cache.put(arg,result);
        }else{
            System.err.println("缓存有值，直接返回缓存内的值");
        }
        return result;
    }
}
