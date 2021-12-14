package com.hoppinzq.service.serviceImpl.serviceImpl;

import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.interfaceService.Test;

import java.io.Serializable;


/**
 * 测试类1
 */
@ServiceRegister
@ApiServiceMapping(title = "ces", description = "ces")
public class TestImpl implements Test,Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    @Override
    @ApiMapping(value = "test")
    public String test(int str) {
//        CacheService service = ServiceProxyFactory.createProxy(CacheService.class, "http://localhost:8080/");
//        System.out.println(service.getApiCache().toJSONString());
        return str+"";
    }

//    public static void main(String[] args) {
//        System.out.println(System.currentTimeMillis());
//    }
//    public void prt(){
//        System.out.println(123213131);
//    }
}
