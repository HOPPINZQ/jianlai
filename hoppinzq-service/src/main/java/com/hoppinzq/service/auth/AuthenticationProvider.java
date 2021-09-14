package com.hoppinzq.service.auth;


import com.hoppinzq.service.common.InvocationRequest;
import com.hoppinzq.service.exception.AuthenticationFailedException;

/**
 * 通过提供的调用请求对调用方进行身份验证
 * 不抛出AuthenticationFailedException异常表示校验通过
 * 目前提供三种验证方式：
 * 1、AuthenticationNotRequiredAuthenticator 不进行验证
 * 2、DatabaseUsernamePasswordAuthenticator 使用数据库内的用户名密码匹配校验
 * 3、SingleUsernamePasswordAuthenticator 使用用户名密码组合的方式校验
 * 每个服务在注册的时候会进行服务包装，通过包装服务的serviceWrapper.setAuthenticationProvider 设置校验手段
 */
public interface AuthenticationProvider {
    /**
     * 身份验证
     * 验证通过将通过AuthenticationContext.setPrincipal(bean)更新身份信息
     * @param invocationRequest
     * @throws AuthenticationFailedException
     */
    void authenticate(InvocationRequest invocationRequest) throws AuthenticationFailedException;

}
