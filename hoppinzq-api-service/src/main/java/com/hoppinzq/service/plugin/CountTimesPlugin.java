package com.hoppinzq.service.plugin;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class CountTimesPlugin implements MethodBeforeAdvice {

    private int count;

    protected void count(Method m){
        count++;
    }

    public void before(Method m,Object[] args,Object target) throws Throwable{
        count(m);
        System.out.println("次数："+count);
    }
}
