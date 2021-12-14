package com.hoppinzq.service.serviceImpl;

import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.interfaceService.LoginService;

import java.io.Serializable;

@ApiServiceMapping(title = "登录认证服务",description = "这是登录认证服务",roleType = ApiServiceMapping.RoleType.NO_RIGHT)
@ServiceRegister
public class LoginServiceImpl implements LoginService,Serializable {

    private static final long serialVersionUID = 2783377098145240357L;

    @Override
    @ApiMapping(value = "login")
    public void login() {
        System.err.println("登陆成功！");
    }

    @Override
    public void logout() {

    }
}
