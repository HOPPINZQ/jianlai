package com.hoppinzq.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.ServiceProxyFactory;
import com.hoppinzq.service.bean.*;
import com.hoppinzq.service.cache.apiCache;
import com.hoppinzq.service.common.UserPrincipal;
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
    public String page(@PathVariable("url") String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(needLoginWebUrl.indexOf(url)!=-1){
            UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
            LoginService loginService= ServiceProxyFactory.createProxy(LoginService.class, rpcPropertyBean.getServerAuth(), upp);
            String token = CookieUtils.getCookieValue(request,"ZQ_TOKEN");
            if (null == token) {
                response.sendRedirect(authUrl + "?redirect=" + request.getRequestURL());
            }else{
                User user = loginService.getUserByToken(token);
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
        JSONObject json = (JSONObject) redisUtils.get("USER:" +token);
        if (json==null) {
            throw new RuntimeException("用户登录已过期");
        }
        return JSONObject.parseObject(JSONObject.toJSONString(json),User.class);
    }

    /**
     * 登出
     */
    @ResponseBody
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response){
        String token = CookieUtils.getCookie(request,"ZQ_TOKEN");
        redisUtils.del("USER:"+token);
        Cookie cookie = new Cookie("ZQ_TOKEN", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
