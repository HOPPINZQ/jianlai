package com.hoppinzq.service.bean;

import com.hoppinzq.service.aop.annotation.ApiMapping;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ServiceMethodApiBean {

    public ApiMapping.RoleType methodRight;
    public boolean methodReturn;
    public String methodTitle;
    public String methodDescription;
    public String serviceMethod;
    public List<Map> serviceMethodParams;
    public Type serviceMethodReturn;
    public Object serviceMethodReturnParams;

}
