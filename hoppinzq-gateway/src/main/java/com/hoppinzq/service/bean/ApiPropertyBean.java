package com.hoppinzq.service.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author:ZhangQi
 * api网关配置类
 **/
@Component
@Scope("singleton")
public class ApiPropertyBean {

    @Value("${zqApiStore.isAuth:false}")
    private Boolean isAuth;

    @Value("${zqApiStore.fileUploadPath:/home/file}")
    private String filePath;

    @Value("${zqAuth.ssoUrl:https://hoppinzq.com}")
    private String ssoUrl;

    @Value("${zqAuth.ssoAdminUrl:https://hoppinzq.com}")
    private String ssoAdminUrl;

    /**
     * 是否允许未登录状态调用（调试用，会在某些需要获取当前登录人的接口中获取不到当前登录人）
     * @return
     */
    public Boolean isAuth() {
        return isAuth;
    }

    public String getSsoAdminUrl() {
        return ssoAdminUrl;
    }

    public String getSsoUrl() {
        return ssoUrl;
    }

    public String getFilePath() {
        return filePath;
    }
}
