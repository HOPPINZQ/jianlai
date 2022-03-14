package com.hoppinzq.service.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author liutf
 * @date 2020-06-16
 */
public class HttpHolderUtils {

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getHttpServletRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request;
    }

    /**
     * 获取response
     * @return
     */
    public static HttpServletResponse getHttpServletResponse(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        return response;
    }
}
