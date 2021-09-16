package com.hoppinzq.service.cache;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author:ZhangQi
 **/
public class ServiceCache<K, V> {

   private final Map<K,V> cache = new ConcurrentHashMap<K,V>();//使用HashMap容器作为缓存容器

    public synchronized V getService(K arg) throws InterruptedException {//给方法加上同步
        V result = cache.get(arg);//尝试从缓存中取计算结果
        if(result==null){         //若缓存中没有计算结果
            System.err.println("缓存没有值,赋值");
            result = (V)"zhangqi";//调用方法进行计算
            cache.put(arg,result);//将计算结果添加到缓存中
        }else{
            System.err.println("缓存有值，直接返回缓存内的值");
        }
        return result;            //放回结果
    }
}
