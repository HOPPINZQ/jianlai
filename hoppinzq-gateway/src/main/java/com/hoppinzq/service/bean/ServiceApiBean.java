package com.hoppinzq.service.bean;

import java.util.List;

/**
 * 服务类参数
 * @author:ZhangQi
 */
public class ServiceApiBean {

    public String apiServiceTitle;
    public String apiServiceDescription;
    public List<ServiceMethodApiBean> serviceMethods;

    public String getApiServiceTitle() {
        return apiServiceTitle;
    }

    public void setApiServiceTitle(String apiServiceTitle) {
        this.apiServiceTitle = apiServiceTitle;
    }

    public String getApiServiceDescription() {
        return apiServiceDescription;
    }

    public void setApiServiceDescription(String apiServiceDescription) {
        this.apiServiceDescription = apiServiceDescription;
    }

    public List<ServiceMethodApiBean> getServiceMethods() {
        return serviceMethods;
    }

    public void setServiceMethods(List<ServiceMethodApiBean> serviceMethods) {
        this.serviceMethods = serviceMethods;
    }
}
