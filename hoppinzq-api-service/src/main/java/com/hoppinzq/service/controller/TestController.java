package com.hoppinzq.service.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:ZhangQi
 **/
@RestController
public class TestController {

    @RequestMapping("/view")
    public String english(Integer type, HttpServletRequest request){
        request.setAttribute("type",type);
        return "english.html";
    }
}
