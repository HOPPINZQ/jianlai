package com.hoppinzq.service.cache;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:ZhangQi
 **/
public class ServiceMessageCache {

    public static Map<String, Object> serviceMessage = new ConcurrentHashMap<>();

    public static synchronized Object getCacheServiceMessage(String arg){
        Object result = serviceMessage.get(arg);
        if(result!=null){
            return result;
        }
        return null;
    }

    static {

    }
}
