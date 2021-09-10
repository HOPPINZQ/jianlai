package com.hoppinzq.service.brap.auth;


import com.hoppinzq.service.brap.common.InvocationRequest;
import com.hoppinzq.service.brap.exception.AuthorizationFailedException;

import java.io.Serializable;

/**
 * 授权所有调用请求的服务提供者类
 * 在注册服务时，通过serviceWrapper.setAuthorizationProvider方法设置服务提供者时，若传入该类，则在调用服务前将不进行调用方身份校验
 * 该方法实现AuthorizationProvider接口并提供一个校验调用方身份的方法authorize
 */
public class AuthenticationNotRequiredAuthorizer implements AuthorizationProvider, Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    public void authorize(InvocationRequest invocationRequest) throws AuthorizationFailedException {
        //不进行身份校验
    }
}
