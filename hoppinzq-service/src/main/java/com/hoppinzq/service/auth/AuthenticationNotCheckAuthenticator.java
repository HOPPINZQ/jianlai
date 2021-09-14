package com.hoppinzq.service.auth;


import com.hoppinzq.service.common.InvocationRequest;

import java.io.Serializable;

/**
 * @author:ZhangQi
 * 身份验证后不需要授权就可以调用服务
 */
public class AuthenticationNotCheckAuthenticator implements AuthenticationProvider, Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    public void authenticate(InvocationRequest invocationRequest) {
    }
}
