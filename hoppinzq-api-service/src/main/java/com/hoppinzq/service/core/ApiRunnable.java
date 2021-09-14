package com.hoppinzq.service.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 网关找到服务并执行之
 * @author:ZhangQi
 **/
public class ApiRunnable {
    /**
     * bit.api.user.getUser
     */
    String apiName;
    /**
     * ioc bean 名称
     */
    String targetName;
    /**
     * UserServiceImpl 实例
     */
    Object target;
    /**
     * 目标方法 getUser
     */
    Method targetMethod;

    /**
     * 多线程安全问题
     * @param args
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public Object run(Object... args) throws Exception {
        if (target == null) { // spring ioc 容器里面去服务Bean 比如GoodsServiceImpl
            target=ApiStore.getApplicationContext().getBean(targetName);
        }
        return targetMethod.invoke(target, args);
    }

    public Class<?>[] getParamTypes() {
        return targetMethod.getParameterTypes();
    }

    public String getApiName() {
        return apiName;
    }

    public String getTargetName() {
        return targetName;
    }

    public Object getTarget() {
        return target;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

}
