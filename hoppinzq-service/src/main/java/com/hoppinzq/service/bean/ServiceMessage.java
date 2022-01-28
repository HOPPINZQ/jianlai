package com.hoppinzq.service.bean;


import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @author:ZhangQi
 * 服务信息实体类
 */
public class ServiceMessage implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    private String serviceIP;
    private String servicePort;
    private String servicePrefix;
    private String serviceTitle;
    private ServerEnum serviceType;
    private String serviceDescription;
    private int timeout;


    public void innerService(String serviceIP,String servicePort,String servicePrefix,String serviceTitle,String serviceDescription,int timeout){
        this.serviceIP=serviceIP;
        this.servicePort=servicePort;
        this.servicePrefix=servicePrefix;
        this.serviceTitle=serviceTitle;
        this.serviceDescription=serviceDescription;
        this.serviceType=ServerEnum.INNER;
        this.timeout=timeout;
    }

    public ServiceMessage(){}
    public ServiceMessage(ServerEnum serviceType){
        this.serviceType=serviceType;
    }

    public ServiceMessage(String serviceIP,String servicePort,String servicePrefix,ServerEnum serviceType){
        this.serviceIP=serviceIP;
        this.servicePort=servicePort;
        this.servicePrefix=servicePrefix;
        this.serviceType=serviceType;
    }
    public ServiceMessage(String serviceIP,String servicePort,String servicePrefix,ServerEnum serviceType,ServiceMessage serviceMessage){
        this.serviceIP=serviceIP;
        this.servicePort=servicePort;
        this.servicePrefix=servicePrefix;
        this.serviceType=serviceType;
        this.timeout=serviceMessage.getTimeout();
    }

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

    public String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }

    public String getServicePrefix() {
        return servicePrefix;
    }

    public void setServicePrefix(String servicePrefix) {
        this.servicePrefix = servicePrefix;
    }

    public Map toJSON(){
        Map serviceMessageMap=new HashMap();
        serviceMessageMap.put("serviceIP", StringUtils.notNull(this.serviceIP));
        serviceMessageMap.put("servicePort", StringUtils.notNull(this.servicePort));
        serviceMessageMap.put("servicePrefix", StringUtils.notNull(this.servicePrefix));
        serviceMessageMap.put("serviceTitle",StringUtils.notNull(this.serviceTitle));
        serviceMessageMap.put("serviceType",this.serviceType.getState());
        serviceMessageMap.put("serviceTypeValue",this.serviceType.getInfo());
        serviceMessageMap.put("serviceDescription",this.serviceDescription);
        serviceMessageMap.put("timeout",timeout);
        return serviceMessageMap;
    }
}
