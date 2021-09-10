package com.hoppinzq.service.brap.service;

import java.io.Serializable;

/**
 * @author:ZhangQi
 * 服务方法实体类
 */
public class ServiceMethodBean implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    private String methodName;
    private String[] methodParamsType;
    private String methodReturnType;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getMethodParamsType() {
        return methodParamsType;
    }

    public void setMethodParamsType(String[] methodParamsType) {
        this.methodParamsType = methodParamsType;
    }

    public String getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
    }
}
