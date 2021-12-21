package com.hoppinzq.service.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Scope("singleton")
public class RPCPropertyBean {

    @Value("${zqServer.ip:127.0.0.1}")
    private String ip;

    @Value("${zqServerCenter.addr:http://127.0.0.1:8801/service}")
    private String serverCenter;

    @Value("${zqServer.userName:zhangqi}")
    private String userName;

    @Value("${zqServer.password:123456}")
    private String password;


    public String getIp() {
        return ip;
    }

    public String getServerCenter() {
        return serverCenter;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
