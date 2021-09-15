package com.hoppinzq.service.client;

import com.hoppinzq.service.common.InputStreamArgument;
import com.hoppinzq.service.common.InvocationRequest;
import com.hoppinzq.service.common.InvocationResponse;
import com.hoppinzq.service.common.ModificationList;
import com.hoppinzq.service.exception.RemotingException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author:ZhangQi
 * 该方法实现提供的服务的接口，获取并发起方法调用，并返回调用结果
 */
public class MethodInvocationHandler implements InvocationHandler, Serializable {
    private String serviceURI;
    private Serializable credentials;
    private static final String REGEXP_PROPERTY_DELIMITER = "\\.";
    private TransportProvider transportProvider;
    private static TransportProvider defaultTransportProvider = new HttpURLConnectionTransportProvider();

    public MethodInvocationHandler() {
    }

    /**
     * @param serviceURI 服务URI
     * @param credentials 凭证，用于用户校验和鉴权
     * @param transportProvider 服务提供者
     */
    public MethodInvocationHandler(String serviceURI, Serializable credentials, TransportProvider transportProvider) {
        this.serviceURI = serviceURI;
        this.credentials = credentials;
        this.transportProvider = transportProvider;
    }


    /**
     * @param serviceURI
     * @param credentials
     */
    public MethodInvocationHandler(String serviceURI, Serializable credentials) {
        this(serviceURI, credentials, null);
    }


    /**
     * 在给定URI上创建服务代理
     * @param serviceURI
     */
    public MethodInvocationHandler(String serviceURI) {
        this(serviceURI, null);
    }

    /**
     * 拦截对代理的方法调用，并通过HTTP发送调用
     * 如果在服务器端抛出异常，它将被重新抛出给调用方。
     * 返回方法调用的返回值。
     * @param obj
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
        InvocationResponse response;
        TransportProvider currentProvider = transportProvider != null ? transportProvider : defaultTransportProvider;
        TransportSession session = currentProvider.createSession(this);

        try {
            InvocationRequest request = new InvocationRequest(method, args, getCredentials());

            // 查找作为输入流的第一个参数，从参数数组中删除参数数据
            // 并准备在序列化调用请求后通过ConnectionOutputStream传输数据。
            InputStream streamArgument = null;
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] != null && InputStream.class.isAssignableFrom(args[i].getClass())) {
                        streamArgument = (InputStream) args[i];
                        args[i] = new InputStreamArgument();
                        break;
                    }
                }
            }
            InputStream inputStream = session.sendInvocationRequest(method, request, streamArgument);

            if (!method.getReturnType().equals(Object.class) && method.getReturnType().isAssignableFrom(InputStream.class))
                return inputStream;

            ObjectInputStream in = new ObjectInputStream(inputStream);
            response = (InvocationResponse) in.readObject();
            applyModifications(args, response.getModifications());
        } catch (IOException e) {
            throw new RemotingException(e);
        } finally {
            currentProvider.endSession(session, this);
        }

        if (response.getException() != null)
            throw response.getException();

        return response.getResult();
    }

    private void applyModifications(Object[] args, ModificationList[] modifications) {
        if (modifications != null) {
            for (int i = 0; i < modifications.length; i++) {
                ModificationList mods = modifications[i];
                if (mods != null) {
                    for (Map.Entry<String, Object> entry : mods.getModifiedProperties().entrySet()) {
                        try {
                            setModifiedValue(entry.getKey(), entry.getValue(), args[i]);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }
    }

    private void setModifiedValue(String key, Object value, Object object) throws NoSuchFieldException, IllegalAccessException {
        String[] propertyGraph = key.split(REGEXP_PROPERTY_DELIMITER);
        int i = 0;

        for (; i < propertyGraph.length - 1; i++)
            object = getValue(object, object.getClass().getDeclaredField(propertyGraph[i]));

        setValue(object, object.getClass().getDeclaredField(propertyGraph[i]), value);
    }

    private void setValue(Object object, Field field, Object value) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        if (!accessible) field.setAccessible(true);
        field.set(object, value);
        if (!accessible) field.setAccessible(false);
    }

    private Object getValue(Object object, Field field) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        if (!accessible) field.setAccessible(true);
        Object value = field.get(object);
        if (!accessible) field.setAccessible(false);
        return value;
    }


    public String getServiceURI() {
        return serviceURI;
    }

    public void setServiceURI(String serviceURI) {
        this.serviceURI = serviceURI;
    }

    public Serializable getCredentials() {
        return credentials;
    }

    public void setCredentials(Serializable credentials) {
        this.credentials = credentials;
    }

    public TransportProvider getTransportProvider() {
        return transportProvider;
    }

    public void setTransportProvider(TransportProvider transportProvider) {
        this.transportProvider = transportProvider;
    }

    public static TransportProvider getDefaultTransportProvider() {
        return defaultTransportProvider;
    }

    public static void setDefaultTransportProvider(TransportProvider defaultTransportProvider) {
        MethodInvocationHandler.defaultTransportProvider = defaultTransportProvider;
    }

}