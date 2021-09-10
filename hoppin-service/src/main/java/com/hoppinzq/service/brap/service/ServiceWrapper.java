package com.hoppinzq.service.brap.service;

import com.hoppinzq.service.brap.auth.AuthenticationProvider;
import com.hoppinzq.service.brap.auth.AuthorizationProvider;
import com.hoppinzq.service.brap.modification.ModificationManager;

import java.io.Serializable;

/**
 * ServiceWrapper保存要通过远程处理公开的服务对象
 */
public class ServiceWrapper implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

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

    public ServiceRegisterBean getServiceRegisterBean() {
        return serviceRegisterBean;
    }

    public void setServiceRegisterBean(ServiceRegisterBean serviceRegisterBean) {
        this.serviceRegisterBean = serviceRegisterBean;
    }
    /* Getters and setters */
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
