package com.hoppinzq.service.brap.auth;



import com.hoppinzq.service.brap.common.InvocationRequest;
import com.hoppinzq.service.brap.common.UsernamePasswordPrincipal;
import com.hoppinzq.service.brap.exception.AuthenticationFailedException;

import java.io.Serializable;

/**
 * 简单的身份验证提供类，在调用服务需要传入，为用户名/密码组合的方法进行身份验证。
 * @see InvocationRequest#getCredentials() 通过该方法可以获取到调用方身份信息
 */
public class SingleUsernamePasswordAuthenticator implements AuthenticationProvider, Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    private String username;
    private String password;

    public SingleUsernamePasswordAuthenticator() {
    }

    public SingleUsernamePasswordAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void authenticate(InvocationRequest invocationRequest) throws AuthenticationFailedException {
        if (invocationRequest.getCredentials() != null && invocationRequest.getCredentials() instanceof UsernamePasswordPrincipal) {
            UsernamePasswordPrincipal upp = (UsernamePasswordPrincipal) invocationRequest.getCredentials();
            if (username.equals(upp.getUsername()) && password.equals(upp.getPassword()))
                AuthenticationContext.setPrincipal(upp);
            else
                throw new AuthenticationFailedException("身份验证失败");
        } else
            throw new AuthenticationFailedException("缺少凭据");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
