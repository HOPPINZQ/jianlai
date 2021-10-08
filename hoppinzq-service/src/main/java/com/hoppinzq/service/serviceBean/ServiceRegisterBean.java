package com.hoppinzq.service.serviceBean;

import com.hoppinzq.service.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:ZhangQi
 * 外部注册服务实体类
 **/
public class ServiceRegisterBean implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    private String serviceName;
    private String serviceFullName;
    private List<ServiceMethodBean> serviceMethodBeanList;
    private Boolean available=Boolean.TRUE;
    private Boolean visible=Boolean.TRUE;
    private Class service;

    public ServiceRegisterBean() {}
    public ServiceRegisterBean(Boolean visible) {
        this.visible = visible;
    }

    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getServiceFullName() {
        return serviceFullName;
    }

    public void setServiceFullName(String serviceFullName) {
        this.serviceFullName = serviceFullName;
    }

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
        this.service=serviceClass;
        this.serviceMethodBeanList=new ArrayList<>();
        serviceFullName=serviceClass.getName();
        serviceName=serviceClass.getSimpleName();
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

    public Map toJSON(){
        Map serviceRegisterBeanMap=new HashMap();
        serviceRegisterBeanMap.put("serviceName", StringUtils.notNull(this.serviceName));
        serviceRegisterBeanMap.put("serviceFullName", StringUtils.notNull(this.serviceFullName));
        serviceRegisterBeanMap.put("serviceMethodBeanList", this.serviceMethodBeanList==null?new ArrayList<>():this.serviceMethodBeanList.stream().map( i -> i.toJSON()).collect(Collectors.toList()));
        serviceRegisterBeanMap.put("class", this.service==null?"":this.service.toString());
        serviceRegisterBeanMap.put("available", this.available);
        serviceRegisterBeanMap.put("availableValue", this.available?"可用":"不可用");
        return serviceRegisterBeanMap;
    }
}
