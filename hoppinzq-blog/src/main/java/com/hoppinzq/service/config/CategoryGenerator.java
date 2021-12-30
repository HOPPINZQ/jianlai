package com.hoppinzq.service.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 缓存自定义key是实体类的情况下，将类名_方法名_参数列表作为key
 * @author: zq
 */
public class CategoryGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String cacheKey=target.getClass().getSimpleName() + "_"
                + method.getName() + "_"
                + StringUtils.arrayToDelimitedString(params, "_");
        return cacheKey;
    }
}