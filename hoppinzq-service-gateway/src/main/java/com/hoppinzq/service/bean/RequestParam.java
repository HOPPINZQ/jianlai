package com.hoppinzq.service.bean;

import com.hoppinzq.service.core.ApiRunnable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * @author:ZhangQi
 * 请求参数封装，为每个线程保存其独有的参数，防止并发下因处理过程中太慢导致参数可能会被覆盖篡改
 */
public class RequestParam {

    public static ThreadLocal<Serializable> requestHold = new ThreadLocal<Serializable>();
    public static String params;
    public static String method;
    public static String sign;
    public static String encode;
    public static String token;
    public static String timestamp;
    public static ApiRunnable apiRunnable;
    public static List<FormInfo> formInfoList;
    public static HttpServletRequest request;
    public static HttpServletResponse response;

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

    public static String getMethod() {
        return method;
    }

    public static String getSign() {
        return sign;
    }

    public static String getEncode() {
        return encode;
    }

    public static String getToken() {
        return token;
    }

    public static String getTimestamp() {
        return timestamp;
    }

    public static List<FormInfo> getList() {
        return formInfoList;
    }

    public static ApiRunnable getApiRunnable() {
        return apiRunnable;
    }

    public static HttpServletRequest getRequest() {
        return request;
    }

    public static HttpServletResponse getResponse() {
        return response;
    }
}
