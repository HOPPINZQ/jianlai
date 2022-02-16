package com.hoppinzq.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.ServiceProxyFactory;
import com.hoppinzq.service.bean.RPCPropertyBean;
import com.hoppinzq.service.bean.User;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.interfaceService.GiteeOAuthService;
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.util.CookieUtils;
import com.hoppinzq.service.util.RedisUtils;
import com.hoppinzq.service.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * 基础用以跳转页面controller
 */
@Controller
public class ZUIController {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RPCPropertyBean rpcPropertyBean;
    @Value("${zqAuth.ssoUrl:http://127.0.0.1:8804/login.html}")
    private String authUrl;
    @Value("${zqAuth.ssoAdminUrl:http://127.0.0.1:8804/adminLogin.html}")
    private String adminUrl;
    @Value("${zqAuth.needLoginWebUrl:}")
    private String needLoginWebUrl;

    @Value("${zqAuth.needAdminWebUrl:}")
    private String needAdminWebUrl;

    @Value("${zqMainPage.url:}")
    private String mainUrl;

    private static final Logger logger = LoggerFactory.getLogger(ZUIController.class);


    /**
     * 页面跳转
     * 1、首页：parallax_index.html
     * @param url
     * @return
     */
    @RequestMapping("{url}.html")
    public String page(@PathVariable("url") String url, String ucode,HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("------------------------------------------------");
        logger.debug("要访问"+url+".html页面了");
        try{
            if(needLoginWebUrl.indexOf(url)!=-1){
                logger.debug("访问的"+url+".html页面需要登录权限");
                logger.debug("连接auth服务开始");
                UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
                LoginService loginService= ServiceProxyFactory.createProxy(LoginService.class, rpcPropertyBean.getServerAuth(), upp);
                logger.debug("连接auth服务成功");
                String token = CookieUtils.getCookieValue(request,"ZQ_TOKEN");
                logger.debug("获取到了ZQ_TOKEN的Cookie的token:"+token);
                if(token==null&&ucode!=null){
                    logger.debug("获取到了一次性ucode:"+ucode);
                    User user =loginService.getUserByCode(ucode);
                    if(user==null){
                        token=null;
                    }else{
                        logger.debug("查询到了ucode对应的用户信息:"+ JSONObject.toJSONString(user));
                        token=user.getToken();
                        logger.debug("查询到了对应的用户信息的token:"+token);
                        //设置token有效期
                        logger.debug("写入值为:ZUI:USER:"+token+"redis中");
                        redisUtils.set("ZUI:USER:"+token,user,7*24*60*60);
                        logger.debug("写入值为:ZQ_TOKEN的Cookie中："+token);
                        Cookie cookie = new Cookie("ZQ_TOKEN", token);
                        cookie.setMaxAge(60*60*24*7);
                        //cookie.setDomain(mainUrl);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        logger.debug("返回"+url+".html页面中");
                        return url+".html";
                    }
                }
                if (null == token) {
                    logger.debug("没有获取到token，将重定向至："+authUrl + "?cz___zauth=1&redirect=" + request.getRequestURL());
                    response.sendRedirect(authUrl + "?cz___zauth=1&redirect=" + request.getRequestURL());
                }else{
                    //应该去auth服务里查询用户，但是返回的是null
                    User user=(User)redisUtils.get("ZUI:USER:"+token);
                    //User user = loginService.getUserByToken("BLOG:USER:"+token);
                    if(null==user){
                        logger.debug("获取到的token没有查询到用户，表示这个token："+token+"已过期");
                        //清空cookie
                        logger.debug("清空这个过期的token");
                        Cookie cookie = new Cookie("ZQ_TOKEN", "");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        logger.debug("重定向至："+authUrl + "?redirect=" + request.getRequestURL());
                        response.sendRedirect(authUrl + "?redirect=" + request.getRequestURL());
                    }
                    if(needAdminWebUrl.indexOf(url)!=-1){
                        logger.debug("访问的"+url+".html页面需要管理员权限");
                        if(user.getUserright()!=2){
                            logger.debug("获取到的用户没有管理员权限");
                            logger.debug("重定向至："+authUrl + "?cz___zauth=3&redirect=" + request.getRequestURL());
                            response.sendRedirect(authUrl + "?cz___zauth=3&redirect=" + request.getRequestURL());
                        }
                    }
                }
            }else{
                //在本页面登录
                if(ucode!=null){
                    logger.debug("获取到了一次性ucode:"+ucode);
                    UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
                    LoginService loginService= ServiceProxyFactory.createProxy(LoginService.class, rpcPropertyBean.getServerAuth(), upp);
                    User user =loginService.getUserByCode(ucode);
                    String token;
                    if(user==null){
                        token=null;
                    }else{
                        logger.debug("查询到了ucode对应的用户信息:"+JSONObject.toJSONString(user));
                        token=user.getToken();
                        logger.debug("查询到了对应的用户信息的token:"+token);
                        //设置token有效期
                        logger.debug("写入值为:ZUI:USER:"+token+"redis中");
                        redisUtils.set("ZUI:USER:"+token,user,7*24*60*60);
                        logger.debug("写入值为:ZQ_TOKEN的Cookie中："+token);
                        Cookie cookie = new Cookie("ZQ_TOKEN", token);
                        cookie.setMaxAge(60*60*24*7);
                        //cookie.setDomain(mainUrl);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        logger.debug("返回"+url+".html页面中");
                        return url+".html";
                    }
                }
            }
        }catch (Exception ex){
            logger.error("登入"+url+"出现异常！");
        }finally {
            logger.debug("返回"+url+".html页面中");
            logger.debug("------------------------------------------------");
            return url+".html";
        }
    }

    @RequestMapping("{url}.errorhtml")
    public String errorPage(@PathVariable("url") String url,HttpServletRequest request, HttpServletResponse response) throws IOException {
        return url+".html";
    }

    /**
     * 页面跳转(二级目录)
     * 二级目录目前是从iframe来的，不用加权限
     * @param module
     * @param url
     * @return
     */
    @RequestMapping("{module}/{url}.html")
    public String page(@PathVariable("module") String module,@PathVariable("url") String url) {
        return module + "/" + url+".html";
    }

    @RequestMapping("zui/system/{url}.html")
    public String zuiPage(@PathVariable("url") String url,HttpServletRequest request) {
        request.setAttribute("urlHtml",url);
        String rUrl=mainUrl+"/"+url+".html";
        logger.debug("返回"+rUrl+"中");
        request.setAttribute("rUrl",rUrl);
        return "zui.html";
    }

    @RequestMapping("/oauth")
    public void oauth(String code,String type,String reurl,HttpServletRequest request,HttpServletResponse response) throws Exception {
        logger.debug("------------------------------------------------");
        logger.debug("oauth接口被调用，使用的是："+type+"鉴权");
        //logout(request,response);//不知道为什么在微信总是空指针，先注释了吧
        UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
        if("gitee".equals(type)){
            GiteeOAuthService giteeOAuthService= ServiceProxyFactory.createProxy(GiteeOAuthService.class, rpcPropertyBean.getServerAuth(), upp);
            logger.debug("开始调用第三方码云服务，请前往auth服务查看日志");
            JSONObject userJson=giteeOAuthService.createGiteeUser(code,null,null);
            logger.debug("码云认证成功，获取到码云用户成功，："+userJson.toJSONString());
            User user=JSONObject.toJavaObject(userJson,User.class);
            String token= UUIDUtil.getUUID();
            logger.debug("生成token："+token+"，设置进redis中，设置的Key是：ZUI:USER:"+token);
            redisUtils.set("ZUI:USER:"+token,user,7*24*60*60);
            logger.debug("设置进redis成功，写入cookie设置的Key是：ZQ_TOKEN，值是:"+token+",请在控制台查看是否设置正确。");
            Cookie cookie = new Cookie("ZQ_TOKEN", token);
            cookie.setMaxAge(60*60*24*7);
            response.addCookie(cookie);
        }
        logger.debug("认证完成，设置token完成，即将重定向至："+(reurl==null?"parallax_index.html":reurl));
        logger.debug("------------------------------------------------");
        response.sendRedirect(mainUrl);
        //return reurl==null?"parallax_index.html":reurl;
    }

    /**
     * 获取当前用户
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUser")
    public User getUser(HttpServletRequest request,HttpServletResponse response) throws InterruptedException {
        logger.debug("------------------------------------------------");
        logger.debug("getUser接口被调用");
        String token = CookieUtils.getCookieValue(request,"ZQ_TOKEN");
        logger.debug("获取到当前用户的token："+token);
        if(token==null){
            logger.error("未获取到token，用户未登录过，");
            throw new RuntimeException("用户未登录");
        }
        User user = (User) redisUtils.get("ZUI:USER:" +token);
        if (user==null) {
            logger.error("未获取到用户，token已过期");
            throw new RuntimeException("用户登录已过期");
        }
        logger.debug("获取到当前用户成功："+user);
        logger.debug("------------------------------------------------");
        return user;
    }

    /**
     * 登出
     */
    @ResponseBody
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response){
        logger.debug("------------------------------------------------");
        logger.debug("logout接口被调用");
        logger.debug("有用户登出");
        String token = CookieUtils.getCookie(request,"ZQ_TOKEN");
        if(token!=null){
            logger.debug("获取到的token是:"+token);
            redisUtils.del("ZUI:USER:"+token);
            logger.debug("从redis移除Key:ZUI:USER:"+token);
            Cookie cookie = new Cookie("ZQ_TOKEN", "");
            logger.debug("清空Key为ZQ_TOKEN的cookie");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            logger.debug("用户开始下线");
            UserPrincipal upp = new UserPrincipal(rpcPropertyBean.getUserName(), rpcPropertyBean.getPassword());
            LoginService loginService= ServiceProxyFactory.createProxy(LoginService.class, rpcPropertyBean.getServerAuth(), upp);
            loginService.logout(token);
            logger.debug("用户下线，登出成功！");
            logger.debug("------------------------------------------------");
        }else{
            logger.debug("没有获取到token");
        }
    }
}
