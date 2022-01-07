package com.hoppinzq.service.common;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 请求是服务调用方的MethodInvocationHandler方法封装发出的，
 * 注册中心跟服务调用方约定的通信协议是在http正文下封装了一段标准的java代码二进制序列化，暂时就叫zq协议>_<
 */
public class InvocationRequest implements Serializable {

    private static ThreadLocal<Serializable> requestHolder = new ThreadLocal<Serializable>();

    public static final Serializable getRequestPrincipal() {
        return requestHolder.get();
    }

    public static final void setRequestPrincipal(Serializable requestPrincipal) {
        requestHolder.set(requestPrincipal);
    }

    /**
     * 服务名（必选）
     */
    private String serviceName;
    /**
     * 要在注册中心上调用的方法名（必选）
     */
    private String methodName;

    /**
     * 方法上的参数类（必选，可为空）
     */
    private Class[] parameterTypes;

    /**
     * 方法调用的参数（必选，可为空）
     */
    private Object[] parameters;

    /**
     * 可选对象，服务调用方的信息跟授权信息，这个可以随意强转成任意类，只要你参数写对
     */
    private Serializable credentials;

    /**
     * 这个可以定制，目前是获取调用方的服务名来给服务名赋值
     * @param method
     * @param parameters
     * @param credentials
     */
    public InvocationRequest(Method method, Object[] parameters, Serializable credentials) {
        int index=method.getDeclaringClass().getName().lastIndexOf(".");
        this.credentials = credentials;
        this.serviceName=method.getDeclaringClass().getName().substring(index+1);
        this.methodName = method.getName();
        this.parameterTypes = method.getParameterTypes();
        this.parameters = parameters;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Serializable getCredentials() {
        return credentials;
    }

    public void setCredentials(Serializable credentials) {
        this.credentials = credentials;
    }
}
