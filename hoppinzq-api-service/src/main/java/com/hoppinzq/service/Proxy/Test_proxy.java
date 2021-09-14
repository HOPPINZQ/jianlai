package com.hoppinzq.service.Proxy;

import com.hoppinzq.service.Proxy.cglib.CglibProxy;
import com.hoppinzq.service.Proxy.jdk.JdkProxy;

public class Test_proxy {
    public static void main(String[] args) {
        User_proxy user1 = new User_proxy();
        JdkProxy jdkProxy = new JdkProxy();
        IUser proxy1 = (IUser)jdkProxy.getProxy(user1);
        proxy1.add();


        User_proxy user = new User_proxy();
        CglibProxy cglibProxy = new CglibProxy();
        IUser proxy2 = (IUser)cglibProxy.getProxy(user);
        proxy2.add();
    }
}
