package com.hoppinzq.service.service;

import com.hoppinzq.service.auth.AuthenticationProvider;
import com.hoppinzq.service.auth.AuthorizationProvider;
import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.modification.ModificationManager;

import java.io.Serializable;

/**
 * @author:ZhangQi
 * ServiceWrapper保存要通过远程处理公开的服务对象
 */
public class ServiceWrapper implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    private static ThreadLocal<Serializable> serviceHolder = new ThreadLocal<Serializable>();

    public static final Serializable getServicePrincipal() {
        return serviceHolder.get();
    }

    public static final void setServicePrincipal(Serializable servicePrincipal) {
        serviceHolder.set(servicePrincipal);
    }


    /**
     * 包装服务的持有者，实际的服务实现。
     */
    private Object service;

    /**
     * 每次方法调用之前都会咨询 身份验证提供者 鉴权
     */
    private AuthenticationProvider authenticationProvider;

    /**
     * 在每次方法调用之前，都会咨询授权提供者
     */
    private AuthorizationProvider authorizationProvider;


    /**
     * 修改管理器将跟踪对参数对象的更改，并重新生成它们
     * 以便在客户端上应用相同的更改。默认的修改管理器
     * 什么也不做。
     */
    private ModificationManager modificationManager;

    private ServiceMessage serviceMessage;
    private ServiceRegisterBean serviceRegisterBean;

    public Boolean isInnerService(){
        return this.serviceMessage.getServiceType() == ServerEnum.INNER;
    }

    public ServiceRegisterBean getServiceRegisterBean() {
        return serviceRegisterBean;
    }

    public ServiceMessage createServiceMessage(){
        return new ServiceMessage();
    }

    public ServiceMessage createInnerServiceMessage(String serviceTitle,String serviceDescription,int timeout){
        ServiceMessage serviceMessage=this.createServiceMessage();
        serviceMessage.innerService(serviceTitle,serviceDescription,timeout);
        return serviceMessage;
    }

    public void setServiceRegisterBean(ServiceRegisterBean serviceRegisterBean) {
        this.serviceRegisterBean = serviceRegisterBean;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }
    public ServiceMessage getServiceMessage() {
        return serviceMessage;
    }

    public void setServiceMessage(ServiceMessage serviceMessage) {
        this.serviceMessage = serviceMessage;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public AuthorizationProvider getAuthorizationProvider() {
        return authorizationProvider;
    }

    public void setAuthorizationProvider(AuthorizationProvider authorizationProvider) {
        this.authorizationProvider = authorizationProvider;
    }

    public ModificationManager getModificationManager() {
        return modificationManager;
    }

    public void setModificationManager(ModificationManager modificationManager) {
        this.modificationManager = modificationManager;
    }
}
