package com.hoppinzq.service.cache;

import com.alibaba.fastjson.JSONArray;
import com.hoppinzq.service.service.ServiceWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:ZhangQi
 **/
@RestController
public class CacheController {

    @RequestMapping("/serviceList")
    public JSONArray getMessage(){
        List<ServiceWrapper> serviceWrapperList=ServiceStore.serviceWrapperList;
        JSONArray jsonArray=new JSONArray();
        for(ServiceWrapper serviceWrapper:serviceWrapperList){
            jsonArray.add(serviceWrapper.toJSON());
        }
        return jsonArray;
    }
}
