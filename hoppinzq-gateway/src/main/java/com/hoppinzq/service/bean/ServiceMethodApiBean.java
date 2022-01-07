package com.hoppinzq.service.bean;

import com.hoppinzq.service.aop.annotation.ApiMapping;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ServiceMethodApiBean {

    public ApiMapping.RoleType methodRight;//方法权限
    public ApiMapping.Type requestType;//请求类型
    //方法是否封装返回值，true为网关统一封装；false为不封装，由原方法返回其返回值
    public boolean methodReturn=true;
    //方法是否需要实现自动幂等（AutoIdempotent注解），true为需要，每个校验成功的token有且只能调用1次；false为不校验
    public boolean tokenCheck=false;
    public String methodTitle;//方法标题（ApiMapping注解）
    public String methodDescription;//方法描述（ApiMapping注解）
    public String serviceMethod;//方法标识（ApiMapping注解）
    public List<Map> serviceMethodParams;//方法参数列表
    public Type serviceMethodReturn;//方法返回值类型
    public Object serviceMethodReturnParams;//方法返回值参数列表
    public int cacheTime=0;
    public boolean isCache=false;

    public ApiMapping.Type getRequestType() {
        return requestType;
    }

    public void setRequestType(ApiMapping.Type requestType) {
        this.requestType = requestType;
    }

    public int getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(int cacheTime) {
        this.cacheTime = cacheTime;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public ApiMapping.RoleType getMethodRight() {
        return methodRight;
    }

    public void setMethodRight(ApiMapping.RoleType methodRight) {
        this.methodRight = methodRight;
    }

    public boolean isMethodReturn() {
        return methodReturn;
    }

    public void setMethodReturn(boolean methodReturn) {
        this.methodReturn = methodReturn;
    }

    public boolean isTokenCheck() {
        return tokenCheck;
    }

    public void setTokenCheck(boolean tokenCheck) {
        this.tokenCheck = tokenCheck;
    }

    public String getMethodTitle() {
        return methodTitle;
    }

    public void setMethodTitle(String methodTitle) {
        this.methodTitle = methodTitle;
    }

    public String getMethodDescription() {
        return methodDescription;
    }

    public void setMethodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public List<Map> getServiceMethodParams() {
        return serviceMethodParams;
    }

    public void setServiceMethodParams(List<Map> serviceMethodParams) {
        this.serviceMethodParams = serviceMethodParams;
    }

    public Type getServiceMethodReturn() {
        return serviceMethodReturn;
    }

    public void setServiceMethodReturn(Type serviceMethodReturn) {
        this.serviceMethodReturn = serviceMethodReturn;
    }

    public Object getServiceMethodReturnParams() {
        return serviceMethodReturnParams;
    }

    public void setServiceMethodReturnParams(Object serviceMethodReturnParams) {
        this.serviceMethodReturnParams = serviceMethodReturnParams;
    }
}
