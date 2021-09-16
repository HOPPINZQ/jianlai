package com.hoppinzq.service.Proxy.cglib;

import com.hoppinzq.service.cache.ServiceStore;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 服务缓存
 * @author:ZhangQi
 */
public class CglibServiceCacheProxy implements MethodInterceptor {

    private Object target;

    public Object getProxy(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        before();
        Object result=null;
        try{
            String serviceName=((Class)objects[0]).getName();
            if(ServiceStore.serviceMap.containsKey(serviceName)){
                result=ServiceStore.serviceMap.get(serviceName);
            }else{
                result = method.invoke(target,objects);
                ServiceStore.serviceMap.put(serviceName,result);
            }
        }catch (Exception ex){
            exception();
        }finally {
            after();
        }
        return result;
    }

    private void before() {

    }

    private void after() {

    }

    private void exception() {

    }
}
