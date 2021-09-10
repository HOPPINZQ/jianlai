package com.hoppinzq.service.brap.service;


import com.hoppinzq.service.brap.enums.ServerEnum;

import java.io.Serializable;

/**
 * @author:ZhangQi
 * 服务信息实体类
 */
public class ServiceMessage implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    private String serviceIP;
    private String serviceTitle;
    private ServerEnum serviceType;
    private String serviceDescription;
    private int timeout;

    public String getServiceIP() {
        return serviceIP;
    }

    public void setServiceIP(String serviceIP) {
        this.serviceIP = serviceIP;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public ServerEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServerEnum serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serivceDescription) {
        this.serviceDescription = serivceDescription;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
