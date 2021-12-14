package com.hoppinzq.service.serviceImpl;

import com.hoppinzq.service.util.IPUtils;
import com.hoppinzq.service.util.UUIDUtil;
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

    @Value("${zqServer.ip:127.0.0.1}")
    private String ip;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getIp() {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("windows")) {
            ip= IPUtils.getIpAddress();
        }else{
            if("127.0.0.1".equals(ip)){
                ip= IPUtils.getIpAddress();
            }
        }
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
