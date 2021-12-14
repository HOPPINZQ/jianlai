package com.hoppinzq.service.service;

import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;

import java.io.Serializable;

@ApiServiceMapping(title = "登录认证服务",description = "这是登录认证服务",type = ApiServiceMapping.Type.NO_RIGHT)
@ServiceRegister
public class LoginServiceImpl implements LoginService,Serializable {

    private static final long serialVersionUID = 2783377098145240357L;

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }
}
