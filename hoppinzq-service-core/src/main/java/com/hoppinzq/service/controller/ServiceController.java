package com.hoppinzq.service.controller;

import com.alibaba.fastjson.JSONArray;
import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.client.ServiceProxyFactory;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.interfaceService.RegisterServer;
import com.hoppinzq.service.interfaceService.Test;
import com.hoppinzq.service.service.HeartbeatService;
import com.hoppinzq.service.service.ServiceWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:ZhangQi
 **/
@RestController
public class ServiceController {

    @RequestMapping("/serviceList")
    public JSONArray getMessage(){
//        HeartbeatService service = ServiceProxyFactory.createProxy(HeartbeatService.class, "http://localhost:8802/service");
//        System.err.println(service.areYouOk());
        List<ServiceWrapper> serviceWrapperList = ServiceStore.serviceWrapperList;
        JSONArray jsonArray=new JSONArray();
        for(ServiceWrapper serviceWrapper:serviceWrapperList){
            if(serviceWrapper.isVisible()){
                jsonArray.add(serviceWrapper.toJSON());
            }
        }
        return jsonArray;
    }

    @RequestMapping("/allServiceList")
    public JSONArray getAllMessage(){
        List<ServiceWrapper> serviceWrapperList= ServiceStore.serviceWrapperList;
        JSONArray jsonArray=new JSONArray();
        for(ServiceWrapper serviceWrapper:serviceWrapperList){
            jsonArray.add(serviceWrapper.toJSON());
        }
        return jsonArray;
    }
}
