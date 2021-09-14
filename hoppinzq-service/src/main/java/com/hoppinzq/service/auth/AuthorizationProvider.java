package com.hoppinzq.service.auth;


import com.hoppinzq.service.common.InvocationRequest;
import com.hoppinzq.service.exception.AuthorizationFailedException;

/**
 * 身份验证通过后授权调用请求
 * 不抛出AuthorizationFailedException异常表示通过授权允许调用。
 * 目前提供两种授权方式：
 * 1、AuthenticationNotCheckAuthorizer 不进行授权直接通过
 * 2、AuthenticationCheckAuthorizer 通过授权才能通过
 * 每个服务在注册的时候会进行服务包装，通过包装服务的serviceWrapper.setAuthorizationProvider 设置授权手段
 * 通过AuthenticationContext.getPrincipal()获取验证通过的用户来进行授权操作
 * 有一些情况下，尽管用户通过了身份验证，但是不一定有权调用，抑或是观察出该用户尝试攻击注册中心或者有不好的行为，通过改写authorize方法以判定是否授权该用户的操作
 */
public interface AuthorizationProvider {

    void authorize(InvocationRequest invocationRequest) throws AuthorizationFailedException;
}
