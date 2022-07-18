package com.hoppinzq.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: zq
 */
@Controller
public class WebController {
    @RequestMapping("{url}.html")
    public String login(@PathVariable("url") String url){
        return url+".html";
    }
}
