package com.hoppinzq.service.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author:ZhangQi
 * 请求参数封装
 */
public class RequestParam {

    private static ThreadLocal<Serializable> requsetHold = new ThreadLocal<Serializable>();
    private static String params;
    private static String method;
    private static String sign;
    private static String encode;
    private static String token;
    private static String isEncodeReturn;
    private static String timestamp;
    private static List<FormInfo> list;

    public static final Serializable getRequestHold() {
        return requsetHold.get();
    }

    public static final void setRequestHold(Serializable requestHold) {
        requsetHold.set(requestHold);
    }

    public static void exit() {
        requsetHold.set(null);
    }

    public static void enter() {
        requsetHold.set(null);
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
