package com.hoppinzq.service.bean;

import com.hoppinzq.service.core.ApiRunnable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * @author:ZhangQi
 */
public class RequestParam implements Serializable{
    private static final long serialVersionUID = 1L;
    private String url;
    private String cacheKey;
    private int cacheTime=0;
    private String params;
    private String method;
    private String sign;
    private String encode;
    private String token;
    private String timestamp;
    private ApiRunnable apiRunnable;
    private List<FormInfo> formInfoList;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(int cacheTime) {
        this.cacheTime = cacheTime;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ApiRunnable getApiRunnable() {
        return apiRunnable;
    }

    public void setApiRunnable(ApiRunnable apiRunnable) {
        this.apiRunnable = apiRunnable;
    }

    public List<FormInfo> getFormInfoList() {
        return formInfoList;
    }

    public void setFormInfoList(List<FormInfo> formInfoList) {
        this.formInfoList = formInfoList;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
