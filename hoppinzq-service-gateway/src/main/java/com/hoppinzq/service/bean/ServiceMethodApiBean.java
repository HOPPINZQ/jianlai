package com.hoppinzq.service.bean;

import com.hoppinzq.service.aop.annotation.ApiMapping;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ServiceMethodApiBean {

    public ApiMapping.RoleType methodRight;//方法权限
    //方法是否封装返回值，true为网关统一封装；false为不封装，由原方法返回其返回值
    public boolean methodReturn;
    //方法是否需要实现自动幂等（AutoIdempotent注解），true为需要，每个校验成功的token有且只能调用1次；false为不校验
    public boolean tokenCheck;
    public String methodTitle;//方法标题（ApiMapping注解）
    public String methodDescription;//方法描述（ApiMapping注解）
    public String serviceMethod;//方法标识（ApiMapping注解）
    public List<Map> serviceMethodParams;//方法参数列表
    public Type serviceMethodReturn;//方法返回值类型
    public Object serviceMethodReturnParams;//方法返回值参数列表

}
