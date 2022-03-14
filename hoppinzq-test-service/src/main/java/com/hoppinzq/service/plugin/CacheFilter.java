package com.hoppinzq.service.plugin;

import com.hoppinzq.service.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

public class CacheFilter implements Filter {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("请求开始---------------------------------------");
//        String timestamp=request.getParameter(ApiCommConstant.TIMESTAMP);
//        if(timestamp!=null){
//            chain.doFilter(request, response);
//        }else{
//            String method = request.getParameter(ApiCommConstant.METHOD);
//            String params = request.getParameter(ApiCommConstant.PARAMS);
//            String md5_key="api_"+ EncryptUtil.MD5(method+params);
//            Object result=redisUtils.get(md5_key);
//            if(result!=null){
//                //取缓存
//            }else{
//                chain.doFilter(request, response);
//                if(method!=null&&params!=null){
//                    ServletOutputStream out = response.getOutputStream();
//                    System.out.println("得到的数据是："+out.toString());
//                    redisUtils.set(out.toString(),60*60*12);
//                }
//            }
//        }
        chain.doFilter(request, response);
        System.out.println("请求结束---------------------------------------");
    }

    @Override
    public void destroy() {

    }
}
