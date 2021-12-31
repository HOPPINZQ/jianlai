package com.hoppinzq.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.ServiceProxyFactory;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.bean.*;
import com.hoppinzq.service.cache.apiCache;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.dao.BlogDao;
import com.hoppinzq.service.interfaceService.CSDNService;
import com.hoppinzq.service.interfaceService.CutWordService;
import com.hoppinzq.service.interfaceService.HelloService;
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.util.CookieUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

}
