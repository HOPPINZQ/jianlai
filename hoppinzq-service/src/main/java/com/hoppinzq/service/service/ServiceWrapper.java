package com.hoppinzq.service.service;

import com.hoppinzq.service.auth.*;
import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.enums.ServiceTypeEnum;
import com.hoppinzq.service.modification.ModificationManager;
import com.hoppinzq.service.modification.NotModificationManager;
import com.hoppinzq.service.modification.SetterModificationManager;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 身份验证
     */
    private AuthenticationProvider authenticationProvider;

    /**
     * 授权
     */
    private AuthorizationProvider authorizationProvider;


    /**
     * 修改管理器将跟踪对参数对象的更改
     */
    private ModificationManager modificationManager;

    private ServiceMessage serviceMessage;
    private ServiceRegisterBean serviceRegisterBean;

    private Boolean visible=Boolean.TRUE;//服务默认可见
    private Boolean available=Boolean.TRUE;//服务默认可用
    private ServiceTypeEnum serviceTypeEnum=ServiceTypeEnum.NORMAL;

    public ServiceTypeEnum getServiceTypeEnum() {
        return serviceTypeEnum;
    }

    public void setServiceTypeEnum(ServiceTypeEnum serviceTypeEnum) {
        this.serviceTypeEnum = serviceTypeEnum;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
        if(this.serviceRegisterBean!=null)
            this.serviceRegisterBean.setAvailable(available);
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
        if(this.serviceRegisterBean!=null)
            this.serviceRegisterBean.setVisible(visible);
    }

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
        this.visible=serviceRegisterBean.isVisible();
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


    public Map toJSON(){
        Map map=new HashMap();
        Map serviceMap=new HashMap();
        if(this.service!=null){
            serviceMap.put("serviceName",this.service.getClass().getName());
            serviceMap.put("serviceSimpleName",this.service.getClass().getSimpleName());
            List list=new ArrayList();
            Method[] methods = this.service.getClass().getDeclaredMethods();
            for(Method method:methods){
                Map methodMap=new HashMap();
                methodMap.put("returnType",method.getReturnType().getSimpleName());
                methodMap.put("methodName",method.getName());
                Class[] params = method.getParameterTypes();
                String[] paramsStr=new String[params.length];
                for(int i=0;i<params.length;i++){
                    paramsStr[i]=params[i].getSimpleName();
                }
                methodMap.put("methodParams",paramsStr);
                list.add(methodMap);
            }
            map.put("service",list);
        }else{
            map.put("service","");
        }
        Map authenticationProviderMap=new HashMap();
        Map authorizationProviderMap=new HashMap();
        Map modificationManagerMap=new HashMap();
        map.put("serviceMessage",this.serviceMessage==null?new HashMap<>():this.serviceMessage.toJSON());
        map.put("serviceRegister",this.serviceRegisterBean==null?new HashMap<>():this.serviceRegisterBean);
        if (this.authenticationProvider instanceof SimpleUserCheckAuthenticator) {
            authenticationProviderMap.put("message","用户名密码组合校验");
        }else if(this.authenticationProvider instanceof DbUserCheckAuthenticator){
            authenticationProviderMap.put("message","数据库表数据校验");
        }else if(this.authenticationProvider instanceof AuthenticationNotCheckAuthenticator){
            authenticationProviderMap.put("message","不校验用户身份");
        }else{
            authenticationProviderMap.put("message","其他方式校验");
        }
        map.put("authenticationProvider",authenticationProviderMap);
        if (this.authorizationProvider instanceof AuthenticationNotCheckAuthorizer) {
            authorizationProviderMap.put("message","授权所有调用");
        }else if(this.authorizationProvider instanceof AuthenticationCheckAuthorizer){
            authorizationProviderMap.put("message","授权通过身份校验的调用");
        }else{
            authorizationProviderMap.put("message","其他方式鉴权");
        }
        map.put("authorizationProvider",authorizationProviderMap);

        if (modificationManager instanceof NotModificationManager) {
            modificationManagerMap.put("message","不跟踪服务对参数的修改");
        }else if(modificationManager instanceof SetterModificationManager){
            modificationManagerMap.put("message","跟踪服务对参数的修改");
        }else{
            modificationManagerMap.put("message","其他方式跟踪");
        }
        map.put("modificationManager",modificationManagerMap);

        return map;
    }
}
