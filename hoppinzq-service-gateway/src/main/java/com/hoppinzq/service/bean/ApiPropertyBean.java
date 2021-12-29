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

    @Value("${zqApiStore.fileUploadPath:/home/file}")
    private String filePath;

    @Value("${zqAuth.ssoUrl:https://hoppinzq.com}")
    private String ssoUrl;

    @Value("${zqAuth.ssoAdminUrl:https://hoppinzq.com}")
    private String ssoAdminUrl;

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
