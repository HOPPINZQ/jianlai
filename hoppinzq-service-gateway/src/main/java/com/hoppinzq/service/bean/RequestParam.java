package com.hoppinzq.service.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author:ZhangQi
 * 请求参数封装，为每个线程保存其独有的参数，防止并发下因处理过程中太慢导致参数可能会被覆盖篡改
 */
public class RequestParam {

    private static ThreadLocal<Serializable> requestHold = new ThreadLocal<Serializable>();
    private static String params;
    private static String method;
    private static String sign;
    private static String encode;
    private static String token;
    private static String isEncodeReturn;
    private static String timestamp;
    private static List<FormInfo> list;

    /**
     * 获取设置的主体，可以强转成设置主体的类
     * @return
     */
    public static final Serializable getRequestHold() {
        return requestHold.get();
    }

    /**
     * 可以设置一个主体，但是必须要有下面的字段，你可以把下面的字段放在一个实体类里，然后用此法设置之
     * @param serializable
     */
    public static final void setRequestHold(Serializable serializable) {
        requestHold.set(serializable);
    }

    /**
     * 线程结束清空
     */
    public static void exit() {
        requestHold.set(null);
    }

    /**
     * 线程开始初始化
     */
    public static void enter() {
        requestHold.set(null);
    }

    public static String getParams() {
        return params;
    }

    public static void setParams(String params) {
        RequestParam.params = params;
    }

    public static String getMethod() {
        return method;
    }

    public static void setMethod(String method) {
        RequestParam.method = method;
    }

    public static String getSign() {
        return sign;
    }

    public static void setSign(String sign) {
        RequestParam.sign = sign;
    }

    public static String getEncode() {
        return encode;
    }

    public static void setEncode(String encode) {
        RequestParam.encode = encode;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        RequestParam.token = token;
    }

    public static String getIsEncodeReturn() {
        return isEncodeReturn;
    }

    public static void setIsEncodeReturn(String isEncodeReturn) {
        RequestParam.isEncodeReturn = isEncodeReturn;
    }

    public static String getTimestamp() {
        return timestamp;
    }

    public static void setTimestamp(String timestamp) {
        RequestParam.timestamp = timestamp;
    }

    public static List<FormInfo> getList() {
        return list;
    }

    public static void setList(List<FormInfo> list) {
        RequestParam.list = list;
    }
}
