package com.hoppinzq.service.brap.auth;

import java.io.Serializable;

/**
 * 匿名主体类，在调用服务时传入该类表示
 */
public class AnonymousPrincipal implements Serializable {
    public String getName() {
        return getClass().getCanonicalName();
    }
}
