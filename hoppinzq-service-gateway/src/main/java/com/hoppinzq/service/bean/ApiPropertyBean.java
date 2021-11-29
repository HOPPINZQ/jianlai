package com.hoppinzq.service.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author:ZhangQi
 **/
@Component
@Scope("singleton")
public class ApiPropertyBean {

    @Value("${zqApiStore.fileUploadPath}")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
