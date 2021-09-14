package com.hoppinzq.service.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:ZhangQi
 * 外部注册服务实体类
 **/
public class ServiceRegisterBean implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    private String serviceName;
    private List<ServiceMethodBean> serviceMethodBeanList;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<ServiceMethodBean> getServiceMethodBeanList() {
        return serviceMethodBeanList;
    }

    public void setServiceMethodBeanList(List<ServiceMethodBean> serviceMethodBeanList) {
        this.serviceMethodBeanList = serviceMethodBeanList;
    }
    public void setServiceClass(Class serviceClass){
        this.serviceMethodBeanList=new ArrayList<>();
        serviceName=serviceClass.getName();
        int index=serviceName.lastIndexOf(".")+1;
        this.serviceName=serviceName.substring(index);
        for (Method m : serviceClass.getDeclaredMethods()) {
            ServiceMethodBean serviceMethodBean=new ServiceMethodBean();
            serviceMethodBean.setMethodName(m.getName());
            serviceMethodBean.setMethodReturnType(m.getReturnType().getSimpleName());
            Class[] cs=m.getParameterTypes();
            String[] strings=new String[cs.length];
            for(int i=0;i<cs.length;i++){
                strings[i]=cs[i].getName();
            }
            serviceMethodBean.setMethodParamsType(strings);
            this.serviceMethodBeanList.add(serviceMethodBean);
        }
    }
}
