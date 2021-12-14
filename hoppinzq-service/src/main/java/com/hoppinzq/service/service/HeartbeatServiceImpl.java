package com.hoppinzq.service.service;

import java.io.Serializable;

/**
 * @author:ZhangQi
 * 心跳服务
 **/
public class HeartbeatServiceImpl implements HeartbeatService, Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    @Override
    public String areYouOk() {
//        System.err.println("getIpAddress:"+ IPUtils.getIpAddress());
//        System.err.println("getIpAddr:"+ IPUtils.getIpAddr());
//        System.err.println("getMyIp:"+ IPUtils.getMyIp());

        return "ok";
    }
}
