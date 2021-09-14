package com.hoppinzq.service.auth;

import com.hoppinzq.service.common.InvocationRequest;
import com.hoppinzq.service.exception.AuthorizationFailedException;

import java.io.Serializable;

/**
 * @author:ZhangQi
 * 授权身份验证的请求
 */
public class AuthenticationCheckAuthorizer implements AuthorizationProvider, Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    /**
     * 我这里这个授权的方法很简单，通过身份验证就通过授权
     * @param invocationRequest
     * @throws AuthorizationFailedException
     */
    public void authorize(InvocationRequest invocationRequest) throws AuthorizationFailedException {
        if (AuthenticationContext.getPrincipal() == null)
            throw new AuthorizationFailedException("请提供有效凭据，然后重试");
    }
}
