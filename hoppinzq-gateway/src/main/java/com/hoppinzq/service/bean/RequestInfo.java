package com.hoppinzq.service.bean;

import com.hoppinzq.service.util.UUIDUtil;

import java.io.Serializable;

/**
 * @author:ZhangQi
 * 请求封装信息类，请求日志类
 **/
public class RequestInfo implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    private String id;
    private String ip;
    private String url;
    private String logLevel;
    private String httpMethod;
    private String classMethod;
    private Object requestParams;
    private Object result;
    private String createTime;
    private Long timeCost;
    private String exception;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public Object getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Object requestParams) {
        this.requestParams = requestParams;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(Long timeCost) {
        this.timeCost = timeCost;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +'\n' +
                "ip=" + ip + '\n' +
                "url=" + url + '\n' +
                "logLevel=" + logLevel + '\n' +
                "httpMethod=" + httpMethod + '\n' +
                "classMethod=" + classMethod + '\n' +
                "requestParams=" + requestParams +'\n' +
                "result=" + result +'\n' +
                "createTime=" + createTime +'\n' +
                "timeCost=" + timeCost +'\n' +
                "exception=" + exception + '\n' +
                '}';
    }

    public RequestInfo(String ip, String url, String logLevel, String classMethod, Object requestParams, Object result, String createTime, Long timeCost,Object exception) {
        this.id= UUIDUtil.getUUID();
        this.ip = ip;
        this.url = url;
        this.logLevel = logLevel;
        this.classMethod = classMethod;
        this.requestParams = requestParams;
        this.result = (result!=null&&result.toString().length()>511)?"返回值太长了,只截取了一部分:"+result.toString().substring(0,500):result;
        this.createTime = createTime;
        this.timeCost = timeCost;
        if(exception instanceof Exception){
            this.exception = ((Exception) exception).getMessage();
        }else if(exception==null) {
            this.exception=null;
        }else{
            this.exception=exception.toString();
        }

    }
}
