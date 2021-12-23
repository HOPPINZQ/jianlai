package com.hoppinzq.service.controller;

import com.hoppinzq.service.ServiceProxyFactory;
import com.hoppinzq.service.bean.RPCPropertyBean;
import com.hoppinzq.service.bean.RequestParam;
import com.hoppinzq.service.bean.User;
import com.hoppinzq.service.common.UserPrincipal;
import com.hoppinzq.service.interfaceService.CSDNService;
import com.hoppinzq.service.interfaceService.CutWordService;
import com.hoppinzq.service.interfaceService.HelloService;
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 基础用以跳转页面controller
 */
@Controller
public class IndexController {
    @Autowired
    private RPCPropertyBean rpcPropertyBean;

    @Value("${zqAuth.ssoUrl:http://127.0.0.1:8804/login.html}")
    private String authUrl;

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

}
