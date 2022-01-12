package com.hoppinzq.service.gateway;

import com.hoppinzq.service.ServiceProxyFactory;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.bean.*;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.core.ApiGatewayHand;
import com.hoppinzq.service.core.ApiStore;
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.service.BlogService;
import com.hoppinzq.service.util.CookieUtils;
import com.hoppinzq.service.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zq
 */
@Component
public class BlogGateway extends ApiGatewayHand {
    private static Logger logger = LoggerFactory.getLogger(ApiGatewayHand.class);
    @Autowired
    private RPCPropertyBean rpcPropertyBean;
    @Autowired
    private ApiPropertyBean apiPropertyBean;
    @Autowired
    private BlogService blogService;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("博客重写的网关初始化中");
    }

    @Override
    public void afterSuccessRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.afterSuccessRequest(request,response);
        System.out.println("博客模块服务请求成功");
    }

    @Override
    public void afterRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.afterRequest(request,response);
        RequestParam requestParam = (RequestParam)RequestContext.getPrincipal();
        //可以至记录报错的日志，或者响应成功的日志只记录极少字段。
        blogService.insertLog(requestParam.getRequestInfo());
        System.out.println("博客模块服务请求完毕");
    }
}
