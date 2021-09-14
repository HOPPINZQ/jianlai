package com.hoppinzq.service.cache.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hoppinzq.service.cache.apiCache;
import org.springframework.stereotype.Service;

/**
 * @author:ZhangQi
 **/
@Service
public class CacheServiceImpl implements CacheService{

    @Override
    public JSONArray getApiCache(){
        return JSONArray.parseArray(JSON.toJSONString(apiCache.outApiList));
    }
}
