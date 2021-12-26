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
