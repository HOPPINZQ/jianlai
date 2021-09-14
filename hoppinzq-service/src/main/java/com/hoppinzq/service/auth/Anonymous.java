package com.hoppinzq.service.auth;

import java.io.Serializable;

/**
 * @author:ZhangQi
 * 匿名主体类，在调用服务时传入该类表示
 */
public class Anonymous implements Serializable {
    public String getName() {
        return getClass().getCanonicalName();
    }
}
