package com.hoppinzq.service.controller;
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

    @RequestMapping("/adminLogin.html")
    public String adminLogin(){
        return "/adminLogin.html";
    }

    @ResponseBody
    @RequestMapping("/{redirect}")
    public String redirect(@PathVariable String redirect){
        return redirect;
    }
}
