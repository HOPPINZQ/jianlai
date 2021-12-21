package com.hoppinzq.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    /**
     * 页面跳转
     * @param url
     * @return
     */
    @RequestMapping("{url}.blog.html")
    public String page(@PathVariable("url") String url) {
        return url+".html";
    }
    /**
     * 页面跳转(二级目录)
     * @param module
     * @param url
     * @return
     */
    @RequestMapping("{module}/{url}.blog.bhtml")
    public String page(@PathVariable("module") String module,@PathVariable("url") String url) {
        return module + "/" + url+".html";
    }

}
