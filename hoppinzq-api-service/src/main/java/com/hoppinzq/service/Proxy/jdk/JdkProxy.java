package com.hoppinzq.service.Proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理
 * @author:ZhangQi
 */
public class JdkProxy implements InvocationHandler {

    private Object target;

    public Object getProxy(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target,args);
        after();
        return result;
    }

    private void before() {

    }

    private void after() {

    }
}