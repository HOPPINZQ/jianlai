package com.hoppinzq.service.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: zq
 */
@Component
@ConfigurationProperties("sms")
public class SmsProperty {
    private String secretId;
    private String secretKey;
    private String reqMethod;
    private int connTimeout;
    private String endpoint;
    private String signMethod;
    private String region;
    private String signName;
    private String sdkAppId;
    private String sessionContext;
    private String registerTemplateId;

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(String sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public String getSessionContext() {
        return sessionContext;
    }

    public void setSessionContext(String sessionContext) {
        this.sessionContext = sessionContext;
    }

    public String getRegisterTemplateId() {
        return registerTemplateId;
    }

    public void setRegisterTemplateId(String registerTemplateId) {
        this.registerTemplateId = registerTemplateId;
    }
}
