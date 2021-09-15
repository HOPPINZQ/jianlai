package com.hoppinzq.service.cache.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hoppinzq.client.ServiceProxyFactory;
import com.hoppinzq.service.aop.annotation.ServiceLimit;

import com.hoppinzq.service.auth.AuthenticationNotCheckAuthenticator;
import com.hoppinzq.service.auth.AuthenticationCheckAuthorizer;
import com.hoppinzq.service.auth.SimpleUserCheckAuthenticator;
import com.hoppinzq.service.cache.apiCache;

import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.enums.ServerEnum;
import com.hoppinzq.service.interfaceService.Test;
import com.hoppinzq.service.modification.NotModificationManager;
import com.hoppinzq.service.service.ServiceMessage;
import com.hoppinzq.service.service.ServiceRegisterBean;
import com.hoppinzq.service.service.ServiceWrapper;
import com.hoppinzq.service.service.TestImpl;
import com.hoppinzq.service.service.server.HelloService;
import com.hoppinzq.service.service.server.RegisterServer;
import com.hoppinzq.service.vo.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author:ZhangQi
 **/
@RestController
@RequestMapping("/cache")
public class CacheController {

    @RequestMapping("/apiCache")
    @ServiceLimit
    public ApiResponse getApiCache(){
        JSONArray array= JSONArray.parseArray(JSON.toJSONString(apiCache.outApiList));
        return ApiResponse.data(array);
    }

    @RequestMapping("/zc")
    @ServiceLimit
    public void zc() throws Exception{
        UserPrincipal upp = new UserPrincipal("zhangqi", "123456");

//        HelloService service = ServiceProxyFactory.createProxy(HelloService.class, "http://localhost:8801/service", upp);
//        System.err.println(service.sayHello("zhangqi"));


        //System.err.println(service.sayHello("zhangqi"));
        //System.err.println(service.sendLargeStream(new FileInputStream(new File("D:\\CronDemo.java"))));


//        try{
//            TestService testService = ServiceProxyFactory.createProxy(TestService.class, "http://localhost:8080/service", upp);
//            System.err.println(testService.test("zhangqi"));
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }



//        ServiceWrapper serviceWrapper=new ServiceWrapper();
//        System.out.println("开始创建包装类");
//        TestImpl test=new TestImpl();
//        ServiceRegisterBean serviceRegisterBean=new ServiceRegisterBean();
////        serviceRegisterBean.setServiceClass(test.getClass());
//        serviceRegisterBean.setServiceClass(test.getClass().getInterfaces()[0]);
//        serviceWrapper.setServiceRegisterBean(serviceRegisterBean);
//        SimpleUserCheckAuthenticator singleUsernamePasswordAuthenticator=new SimpleUserCheckAuthenticator();
//        singleUsernamePasswordAuthenticator.setUsername("zhangqi");
//        singleUsernamePasswordAuthenticator.setPassword("123456");
//        serviceWrapper.setAuthenticationProvider(singleUsernamePasswordAuthenticator);
//        ServiceMessage serviceMessage=new ServiceMessage();
//        serviceMessage.setServiceIP("127.0.0.1");
//        serviceMessage.setServiceType(ServerEnum.OUTER);
//        serviceWrapper.setServiceMessage(serviceMessage);
//
//        //serviceWrapper.setService(test.getClass().getInterfaces()[0]);
//
//        if (serviceWrapper.getServiceMessage() == null)
//            serviceWrapper.setServiceMessage(new ServiceMessage());
//        if (serviceWrapper.getAuthenticationProvider() == null)
//            serviceWrapper.setAuthenticationProvider(new AuthenticationNotCheckAuthenticator());
//        if (serviceWrapper.getAuthorizationProvider() == null)
//            serviceWrapper.setAuthorizationProvider(new AuthenticationCheckAuthorizer());
//        if (serviceWrapper.getModificationManager() == null)
//            serviceWrapper.setModificationManager( new NotModificationManager());
//        RegisterServer registerServer = ServiceProxyFactory.createProxy(RegisterServer.class, "http://localhost:8801/service", upp);
//        registerServer.insertService(serviceWrapper);
//
//
        Test test1 = ServiceProxyFactory.createProxy(Test.class, "http://localhost:8801/service", upp);
        System.err.println(test1.test(1));

    }

}
