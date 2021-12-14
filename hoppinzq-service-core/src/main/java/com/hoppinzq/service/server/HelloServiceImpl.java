package com.hoppinzq.service.server;


import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.auth.Anonymous;
import com.hoppinzq.service.auth.AuthenticationContext;
import com.hoppinzq.service.bean.TestBean;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.interfaceService.HelloService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

@ServiceRegister
@ApiServiceMapping(title = "注册中心欢迎",description = "测试注册中心欢迎",type = ApiServiceMapping.Type.NO_RIGHT)
public class HelloServiceImpl implements HelloService {

    @ApiMapping(title = "你hao",value = "hello")
    public String sayHello(String name) {
        Serializable principal = AuthenticationContext.getPrincipal();
        if (principal instanceof UserPrincipal) {
            UserPrincipal upp = (UserPrincipal) AuthenticationContext.getPrincipal();
            return "Hello, " + name + ", 授权用户名 " + upp.getUsername() + " 密码 " + upp.getPassword();
        } else if (principal instanceof Anonymous) {
            return "Hello, " + name + ", 已通过匿名身份验证";
        } else {
            return "Hello, " + name;
        }
    }

    //入参是输入流的
    public String sendLargeStream(InputStream in) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            String content = baos.toString();
            in.close();
            baos.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return  e.getMessage();
        }
    }

    //入参无返回值的
    public void testChangeWithoutReturn(TestBean testBean) {
        Serializable principal = AuthenticationContext.getPrincipal();
        if (principal instanceof UserPrincipal) {
            UserPrincipal upp = (UserPrincipal) AuthenticationContext.getPrincipal();
            testBean.setAge(1);
            testBean.setUserName(upp.getUsername());
            testBean.setPassword(upp.getPassword());
        }else{
            testBean.setAge(1);
            testBean.setUserName("no");
            testBean.setPassword("no");
        }
    }

    @Override
    public TestBean testChange(TestBean testBean) {
        Serializable principal = AuthenticationContext.getPrincipal();
        if (principal instanceof UserPrincipal) {
            UserPrincipal upp = (UserPrincipal) AuthenticationContext.getPrincipal();
            testBean.setAge(1);
            testBean.setPassword(upp.getPassword());
            testBean.setUserName(upp.getUsername());
        } else {
            testBean.setAge(2);
            testBean.setPassword("qwe");
            testBean.setUserName("zq");
        }
        return testBean;
    }
}
