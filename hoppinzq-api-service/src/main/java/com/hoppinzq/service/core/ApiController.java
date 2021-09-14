package com.hoppinzq.service.core;//package com.ganinfo.common.api.core;
//
//import com.ganinfo.common.api.constant.ApiCommConstant;
//import com.ganinfo.common.exception.ApiException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.context.support.WebApplicationContextUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
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
//    public HttpServletRequest getRequest() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        return request;
//    }
//
//
//
//
//}
