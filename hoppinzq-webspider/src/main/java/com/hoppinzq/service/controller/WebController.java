package com.hoppinzq.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.ServiceApiBean;
import com.hoppinzq.service.cache.apiCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: zq
 */
@Controller
public class WebController {
    @RequestMapping("{url}.html")
    public String login(@PathVariable("url") String url){
        return url+".html";
    }

    @ResponseBody
    @RequestMapping("/apiParams")
    public JSONObject getServiceMessage(){
        JSONObject jsonObject=new JSONObject();
        List<ServiceApiBean> serviceApiBeans= apiCache.outApiList;
        jsonObject.put("api",serviceApiBeans);
        return jsonObject;
    }
}
