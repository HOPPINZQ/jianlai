package com.hoppinzq.service.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.bean.SpiderLink;
import com.hoppinzq.service.interfaceService.SpiderService;
import com.hoppinzq.service.spider.SpiderWoker;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * @author: zq
 */
//@ServiceRegister
@ApiServiceMapping(title = "蜘蛛爬虫", description = "蜘蛛爬虫",roleType = ApiServiceMapping.RoleType.NO_RIGHT)
public class SpiderServiceImpl implements SpiderService {
    @Autowired
    private SpiderWoker spiderWoker;

    @ApiMapping(value = "startWork", title = "爬取网站链接关键字", description = "爬取网站链接关键字")
    public void startWork(String url){
        spiderWoker.startWork(url);
    }
    @ApiMapping(value = "queryweb", title = "查询网站", description = "根据关键词查询网站")
    public JSONArray queryweb(String search) {
        List<SpiderLink> spiderLinks=spiderWoker.queryweb(search);
        return JSONArray.parseArray(JSONObject.toJSONString(spiderLinks));
    }

}
