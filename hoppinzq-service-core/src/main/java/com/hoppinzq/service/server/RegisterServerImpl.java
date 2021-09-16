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

    @Override
    public int updateServices(List<ServiceWrapper> serviceWrappers) {
        int index=0;
        for(ServiceWrapper serviceWrapper:serviceWrappers){
            ServiceRegisterBean serviceRegisterBean=serviceWrapper.getServiceRegisterBean();
            for(ServiceWrapper serviceWrapper1:serviceWrapperList){
                ServiceMessage serviceMessage1=serviceWrapper1.getServiceMessage();
                if(serviceMessage1.getServiceType()==ServerEnum.OUTER){
                    ServiceRegisterBean serviceRegisterBean1=serviceWrapper1.getServiceRegisterBean();
                    if(serviceRegisterBean1.getServiceName().equals(serviceRegisterBean.getServiceName())){
                        serviceWrapper1=serviceWrapper;
                        index++;
                    }
                }
            }
        }
        return index;
    }

    @Override
    public void deleteServices(List<ServiceWrapper> serviceWrappers) {

    }

    @Override
    public void queryServices(ServiceWrapper serviceWrapper) {

    }
}
