package com.hoppinzq.service.server;

import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.brap.enums.ServerEnum;
import com.hoppinzq.service.brap.service.ServiceMessage;
import com.hoppinzq.service.brap.service.ServiceWrapper;
import com.hoppinzq.service.cache.ServiceStore;

import java.util.List;

@ServiceRegister
public class RegisterServerImpl implements RegisterServer{

    List<ServiceWrapper> serviceWrapperList= ServiceStore.serviceWrapperList;

    @Override
    public void insertService(ServiceWrapper serviceWrapper){
        ServiceMessage serviceMessage=serviceWrapper.getServiceMessage();
        serviceMessage.setServiceIP("127.0.0.1");
        serviceMessage.setServiceType(ServerEnum.OUTER);
        serviceWrapperList.add(serviceWrapper);
    }

    @Override
    public void insertServices(List<ServiceWrapper> serviceWrappers){
        for(ServiceWrapper serviceWrapper:serviceWrappers){
            insertService(serviceWrapper);
        }
    }
}
