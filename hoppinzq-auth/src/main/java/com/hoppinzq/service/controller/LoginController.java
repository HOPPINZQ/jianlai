package com.hoppinzq.service.controller;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.User;
import com.hoppinzq.service.interfaceService.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @RequestMapping("/login.html")
    public String login(){
        return "/login.html";
    }

    @RequestMapping("/zauth.html")
    public String zauth(){
        return "/zauth.html";
    }

    @RequestMapping("/login1.html")
    public String login1(){
        return "/login1.html";
    }

    @RequestMapping("/adminLogin.html")
    public String adminLogin(){
        return "/adminLogin.html";
    }

    @ResponseBody
    @RequestMapping("/{redirect}")
    public String redirect(@PathVariable String redirect){
        return redirect;
    }

    @Autowired
    private LoginService service;
    /**
     * jsonP接口
     */
    @ResponseBody
    @RequestMapping("/jsonP2Cookie")
    public String jsonp(String zqtoken,String callback){
        User u=service.getUserByToken(zqtoken);
        return callback+"('"+ JSONObject.toJSONString(u) +"')";
    }
}
