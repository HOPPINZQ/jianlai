package com.hoppinzq.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.ServiceProxyFactory;
import com.hoppinzq.service.bean.*;
import com.hoppinzq.service.cache.apiCache;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.interfaceService.GiteeOAuthService;
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.util.CookieUtils;
import com.hoppinzq.service.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 基础用以跳转页面controller
 */
@Controller
public class IndexController {
    @Autowired
    private RPCPropertyBean rpcPropertyBean;

    @Value("${zqAuth.ssoUrl:http://127.0.0.1:8804/login.html}")
    private String authUrl;

    @Value("${zqAuth.ssoAdminUrl:http://127.0.0.1:8804/adminLogin.html}")
    private String adminUrl;

    @Value("${zqAuth.needLoginWebUrl:}")
    private String needLoginWebUrl;

    @Value("${zqAuth.needMemberWebUrl:}")
    private String needMemberWebUrl;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 页面跳转
     * 1、首页：index.html
     * @param url
     * @return
     */
    @RequestMapping("{url}.html")
    public String page(@PathVariable("url") String url, String ucode,String code,HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(needLoginWebUrl.indexOf(url)!=-1){
            UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
            LoginService loginService= ServiceProxyFactory.createProxy(LoginService.class, rpcPropertyBean.getServerAuth(), upp);
            String token = CookieUtils.getCookieValue(request,"ZQ_TOKEN");

//            GiteeOAuthService giteeOAuthService=ServiceProxyFactory.createProxy(GiteeOAuthService.class, rpcPropertyBean.getServerAuth(), upp);
//            String acctoken=giteeOAuthService.getAccessToken("0d7640fba64e051e2540173d60f48a4c987772e2b9a9457e9ab80174f2b607ad","http://hoppinzq.com");
//            if(acctoken!=null){
//                //授权成功！
//            }
//            System.err.println(acctoken);
            if(token==null&&ucode!=null){
                User user =loginService.getUserByCode(ucode);
                if(user==null){
                    token=null;
                }else{
                    token=user.getToken();
                    //设置token有效期
                    redisUtils.set("BLOG:USER:"+token,user,7*24*60*60);
                    Cookie cookie = new Cookie("ZQ_TOKEN", token);
                    cookie.setMaxAge(60*60*24*7);
                    response.addCookie(cookie);
                    return url+".html";
                }
            }
            if (null == token) {
                response.sendRedirect(authUrl + "?redirect=" + request.getRequestURL());
            }else{
                User user = loginService.getUserByToken("BLOG:USER:"+token);
                if(null==user){
                    response.sendRedirect(authUrl + "?redirect=" + request.getRequestURL());
                }
            }
        }
        return url+".html";
    }

    /**
     * 页面跳转(二级目录)
     * @param module
     * @param url
     * @return
     */
    @RequestMapping("{module}/{url}.bhtml")
    public String page(@PathVariable("module") String module,@PathVariable("url") String url) {
        return module + "/" + url+".html";
    }


    @RequestMapping("blog/{blogId}")
    public String blogDeatil(@PathVariable("blogId") String blogId) {
        return "blogDetail.html";
    }


    @ResponseBody
    @RequestMapping("/apiParams")
    public JSONObject getServiceMessage(){
        JSONObject jsonObject=new JSONObject();
        List<ServiceApiBean> serviceApiBeans=apiCache.outApiList;
        jsonObject.put("api",serviceApiBeans);
        return jsonObject;
    }

    /**
     * 获取当前用户
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUser")
    public User getUser(HttpServletRequest request,HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request,"ZQ_TOKEN");
        if(token==null){
            throw new RuntimeException("用户未登录");
        }
        User user = (User) redisUtils.get("BLOG:USER:" +token);
        if (user==null) {
            throw new RuntimeException("用户登录已过期");
        }
        return user;
    }

    /**
     * 登出
     */
    @ResponseBody
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response){
        String token = CookieUtils.getCookie(request,"ZQ_TOKEN");
        redisUtils.del("BLOG:USER:"+token);
        Cookie cookie = new Cookie("ZQ_TOKEN", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
