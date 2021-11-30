package com.hoppinzq.service.core;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// *
// */
//@RestController
//public class ApiController {
//
//    @Autowired
//    private ApiGatewayHand apiHand;
//
//    /**
//     * 所有post请求
//     * @return
//     */
//    @PostMapping("/api")
//    public Object apiPost(String method,String params){
//        return  apiHand.handle(getRequest());
//    }
//
//    /**
//     *所有get请求
//     * @return
//     */
//    @GetMapping("/api")
//    public Object apiGet(){
//        return  apiHand.handle(getRequest());
//    }
//
//    public static HttpServletRequest getRequest() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        return request;
//    }
//
//}
