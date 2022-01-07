package com.hoppinzq.service.server;

import com.hoppinzq.service.aop.annotation.ServiceRegister;

import com.hoppinzq.service.cache.ServiceStore;
import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.enums.ServiceTypeEnum;
import com.hoppinzq.service.interfaceService.RegisterServer;
import com.hoppinzq.service.bean.ServiceMessage;
import com.hoppinzq.service.bean.ServiceRegisterBean;
import com.hoppinzq.service.bean.ServiceWrapper;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@ServiceRegister
public class RegisterServerImpl implements RegisterServer {
    @Value("${zqServer.isStrict:false}")
    private Boolean isStrict;

    List<ServiceWrapper> serviceWrapperList= ServiceStore.serviceWrapperList;

    @Override
    public void insertService(ServiceWrapper serviceWrapper){
        ServiceWrapper wrapper=checkOuterService(serviceWrapper);
        if(wrapper!=null){
            if(isStrict){
                throw new RuntimeException("该服务已注册，必须通过updateServices更新注册！");
            }else{
                serviceWrapperList.remove(wrapper);
            }
        }
        serviceWrapperList.add(serviceWrapper);
        if(serviceWrapper.getService()==null&&serviceWrapper.getServiceTypeEnum()== ServiceTypeEnum.HEARTBEAT){
            ServiceStore.heartbeatService.add(serviceWrapper);
        }
    }

    private ServiceWrapper checkOuterService(ServiceWrapper serviceWrapper){
        ServiceRegisterBean serviceRegisterBean=serviceWrapper.getServiceRegisterBean();
        for(ServiceWrapper wrapper:serviceWrapperList){
            ServiceRegisterBean registerBean=wrapper.getServiceRegisterBean();
            if(serviceRegisterBean.getServiceName().equals(registerBean.getServiceName())){
                return wrapper;
            }
        }
        return null;
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
    public String queryServices(ServiceWrapper serviceWrapper) {
        if(checkOuterService(serviceWrapper)!=null){
            return "有该服务";
        }else{
            return "无该服务";
        }
    }

    @Override
    public String serviceOk() {
        return "RegisterServer服务可用";
    }
}
