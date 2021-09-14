package com.hoppinzq.service.server;

import com.hoppinzq.service.aop.annotation.ServiceRegister;

import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.interfaceService.RegisterServer;
import com.hoppinzq.service.service.ServiceMessage;
import com.hoppinzq.service.service.ServiceRegisterBean;
import com.hoppinzq.service.service.ServiceWrapper;

import java.util.List;

@ServiceRegister
public class RegisterServerImpl implements RegisterServer {

    List<ServiceWrapper> serviceWrapperList= ServiceStore.serviceWrapperList;

    @Override
    public void insertService(ServiceWrapper serviceWrapper){
        if(checkOuterService(serviceWrapper)){
            throw new RuntimeException("该服务已注册！");
        }
        ServiceMessage serviceMessage=serviceWrapper.getServiceMessage();
        serviceMessage.setServiceIP("127.0.0.1");
        serviceMessage.setServiceType(ServerEnum.OUTER);
        serviceWrapperList.add(serviceWrapper);
    }

    private Boolean checkOuterService(ServiceWrapper serviceWrapper){
        ServiceRegisterBean serviceRegisterBean=serviceWrapper.getServiceRegisterBean();
        for(ServiceWrapper wrapper:serviceWrapperList){
            ServiceRegisterBean registerBean=wrapper.getServiceRegisterBean();
            if(serviceRegisterBean.getServiceName().equals(registerBean.getServiceName())){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public void insertServices(List<ServiceWrapper> serviceWrappers){
        for(ServiceWrapper serviceWrapper:serviceWrappers){
            insertService(serviceWrapper);
        }
    }
}
