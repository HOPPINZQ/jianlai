package com.hoppinzq.service.brap.auth;


import com.hoppinzq.service.brap.common.InvocationRequest;

import java.io.Serializable;

/**
 * 身份验证后不需要授权就可以调用服务
 */
public class AuthenticationNotRequiredAuthenticator implements AuthenticationProvider, Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    public void authenticate(InvocationRequest invocationRequest) {
    }
}
