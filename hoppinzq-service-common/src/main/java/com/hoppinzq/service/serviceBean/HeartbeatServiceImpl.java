package com.hoppinzq.service.serviceBean;

import com.hoppinzq.service.util.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * @author:ZhangQi
 * 心跳服务
 **/
public class HeartbeatServiceImpl implements HeartbeatService, Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    @Value("${server.port:8080}")
    private String port;

    @Value("${zqServer.prefix:/service}")
    private String prefix;

    @Override
    public String areYouOk() {
        String serviceAddress="http://"+ IPUtils.getIpAddress() +":"+port+prefix;
        return serviceAddress;
    }
}
