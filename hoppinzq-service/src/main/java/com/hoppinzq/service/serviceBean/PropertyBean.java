package com.hoppinzq.service.serviceBean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author:ZhangQi
 **/
@Component
@Scope("singleton")
public class PropertyBean {

    @Value("${server.port:8080}")
    private String port;

    @Value("${zqServer.prefix:/service}")
    private String prefix;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
