package com.hoppinzq.service.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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


    /**
     * 权限校验
     * 重写该方法以实现自己的权限校验
     * 返回true校验通过
     * 该方法需要注册中心和auth服务，耦合非常高，你可以通过继承本类，并重写rightCheck方法解耦
     * 但是我的所有模块都是用的这个auth服务去做的用户认证，为了避免代码重复，就写在这里了
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public Boolean rightCheck(HttpServletRequest request,HttpServletResponse response,RequestParam requestParam) throws IOException{
        System.out.println("博客模块自己校验权限");
        if(apiPropertyBean.isAuth()){
            return true;
        }
        LoginUser.enter();
        ServiceMethodApiBean serviceMethodApiBean=requestParam.getApiRunnable().getServiceMethodApiBean();
        if(serviceMethodApiBean.methodRight != ApiMapping.RoleType.NO_RIGHT){
            String token=requestParam.getToken()==null?CookieUtils.getCookieValue(request,"ZQ_TOKEN")
                    :requestParam.getToken();

            UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
            LoginService loginService= ServiceProxyFactory.createProxy(LoginService.class, rpcPropertyBean.getServerAuth(), upp);
            User user = loginService.getUserByToken("BLOG:USER:"+token);
            if (null == user) {
                redirectUrl(request,response);
                return false;
            }else{
                if(serviceMethodApiBean.methodRight== ApiMapping.RoleType.ADMIN&&user.getUserright()!=1){
                    redirectUrl(request,response);
                    return false;
                }
            }
            JSONObject userJSON= JSONObject.parseObject(JSON.toJSONString(user));
            request.setAttribute("user", userJSON);
            LoginUser.setUserHold(userJSON);
        }
        return true;
    }

    /**
     * 重定向url
     * @param request
     * @param response
     * @throws IOException
     */
    private void redirectUrl(HttpServletRequest request,HttpServletResponse response) throws IOException {
        RequestParam requestParam= (RequestParam)RequestContext.getPrincipal();
        ServiceMethodApiBean serviceMethodApiBean=requestParam.getApiRunnable().getServiceMethodApiBean();
        //Ajax请求
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            String sourceUrl=request.getHeader("Referer");
            if(null==sourceUrl){
                sourceUrl=request.getRequestURL().toString();
            }
            if(serviceMethodApiBean.methodRight== ApiMapping.RoleType.ADMIN){
                response.setHeader("redirect", apiPropertyBean.getSsoAdminUrl() + "?redirect=" +sourceUrl);
            }else{
                response.setHeader("redirect", apiPropertyBean.getSsoUrl() + "?redirect=" +sourceUrl);
            }
            response.setHeader("enableRedirect","true");
            response.addHeader("Access-Control-Expose-Headers","redirect,enableRedirect,isAdmin");
            response.setStatus(302);
            response.flushBuffer();
        }
        //浏览器地址栏请求
        else {
            String queryString =request.getQueryString();
            String requestURL=String.valueOf(request.getRequestURL());
            String realUrl=requestURL+"?"+queryString;
            //跳转到登录页面，把用户请求的url作为参数传递给登录页面。
            if(serviceMethodApiBean.methodRight== ApiMapping.RoleType.ADMIN){
                response.sendRedirect(apiPropertyBean.getSsoAdminUrl() + "?redirect=" + realUrl);
            }else{
                response.sendRedirect(apiPropertyBean.getSsoUrl() + "?redirect=" + realUrl);
            }
        }
    }
}
